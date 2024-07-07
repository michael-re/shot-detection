package mre.vsbds.gui.panel;

import mre.vsbds.core.analyzer.TwinComparison;
import mre.vsbds.core.shot.Shot;
import mre.vsbds.core.util.Precondition;
import mre.vsbds.core.video.FrameIterator;
import mre.vsbds.gui.util.Border;
import mre.vsbds.gui.util.Layout;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class Pages extends JPanel
{
    private static final int SHOT_ROW_COUNT = 5;
    private static final int SHOT_COL_COUNT = 4;
    private static final int SHOTS_PER_PAGE = SHOT_ROW_COUNT * SHOT_COL_COUNT;

    private final TwinComparison twinComparison;
    private final AtomicInteger  currentPage;
    private final int            pageCount;
    private final List<View>     views;

    public Pages(final TwinComparison twinComparison)
    {
        Precondition.nonNull(twinComparison);
        final var shots     = twinComparison.allShots();
        final var pageCount = (int) Math.ceil(shots.size() / (float) SHOTS_PER_PAGE);
        final var viewCount = pageCount * SHOTS_PER_PAGE;
        final var views     = new ArrayList<View>(viewCount);

        this.twinComparison = twinComparison;
        this.currentPage    = new AtomicInteger(-1);
        this.pageCount      = pageCount;

        shots.forEach(shot -> views.add(new ShotView(shot)));
        while (views.size() < viewCount) views.add(new EmptyView());

        this.views = Collections.unmodifiableList(views);
        this.showPage(0);
    }

    public void showPage(final int page)
    {
        if ((currentPage.get() != page) && validPage(page))
        {
            this.removeAll();
            this.setLayout(Layout.migLayout(SHOT_ROW_COUNT, SHOT_COL_COUNT));

            for (var row = 0; row < SHOT_ROW_COUNT; row++)
            {
                for (var col = 0; col < SHOT_COL_COUNT; col++)
                {
                    final var index = (page * SHOTS_PER_PAGE) + (col * SHOT_ROW_COUNT) + row;
                    final var view  = views.get(index);
                    this.add(view, "cell " + row + " " + col + " 1 1");
                }
            }

            currentPage.set(page);
            this.revalidate();
            this.repaint();
        }
    }

    public void showPrev()
    {
        showPage(currentPage.get() - 1);
    }

    public void showNext()
    {
        showPage(currentPage.get() + 1);
    }

    private boolean validPage(final int pageIndex)
    {
        return !views.isEmpty() && (pageIndex >= 0 && pageIndex < pageCount);
    }

    public void onViewClick(final Consumer<View> action)
    {
        Precondition.nonNull(action);
        views.forEach(view -> view.onClick(action));
    }

    public abstract static class View extends JLabel
    {
        protected Icon thumbnail()                     { return null; }
        protected FrameIterator iterator()             { return null; }
        protected void onClick(final Consumer<View> a) {              }
    }

    private static final class EmptyView extends View
    {
        private EmptyView()
        {
            final var border = Border.titleBorder("  ");
            this.setBorder(Border.compoundBorder(border));
            this.setLayout(Layout.gridBagLayout());

            final var button = new JButton();
            button.setFocusCycleRoot(false);
            button.setVisible(false);
            this.add(button);
        }
    }

    private final class ShotView extends View
    {
        private final FrameIterator iterator;
        private final Icon          thumbnail;
        private final JButton       button;

        private ShotView(final Shot shot)
        {
            Precondition.nonNull(shot);
            final var iterator = twinComparison.threshold().video().iterator(shot);

            iterator.next();
            final var icon      = iterator.smallIcon();
            final var thumbnail = iterator.largeIcon();

            this.iterator    = iterator.reset();
            this.button      = new JButton(Precondition.nonNull(icon));
            this.thumbnail   = Precondition.nonNull(thumbnail);

            final var border = Border.titleBorder("start frame: " + shot.firstFrame());
            this.setBorder(Border.compoundBorder(border));
            this.setLayout(Layout.gridBagLayout());

            button.setFocusCycleRoot(false);
            button.setMargin(new Insets(5, 5, 5, 5));
            this.add(button);
        }

        @Override protected Icon thumbnail()
        {
            return thumbnail;
        }

        @Override
        protected FrameIterator iterator()
        {
            return iterator;
        }

        @Override
        protected void onClick(final Consumer<View> action)
        {
            Precondition.nonNull(action);
            button.addActionListener(_ -> action.accept(this));
        }
    }
}
