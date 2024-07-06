package mre.vsbds.gui.panel;

import mre.vsbds.core.analyzer.TwinComparison;
import mre.vsbds.core.util.Precondition;
import mre.vsbds.gui.util.Layout;

import javax.swing.JPanel;

import java.util.function.Consumer;

public final class Gallery extends JPanel
{
    private final Pages          pages;
    private final TwinComparison twinComparison;

    public Gallery(final TwinComparison twinComparison)
    {
        this.twinComparison = Precondition.nonNull(twinComparison);
        this.pages          = new Pages(twinComparison);

        this.setLayout(Layout.borderLayout());
        this.add(pages, Layout.borderLayoutCenter());

        this.revalidate();
        this.repaint();
    }

    public TwinComparison twinComparison()
    {
        return twinComparison;
    }

    public void showPrevPage()
    {
        pages.showPrev();
    }

    public void showNextPage()
    {
        pages.showNext();
    }

    public void reset()
    {
        pages.showPage(0);
    }

    public void onViewClick(final Consumer<Pages.View> action)
    {
        Precondition.nonNull(action);
        pages.onViewClick(action);
    }
}
