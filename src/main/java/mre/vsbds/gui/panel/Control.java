package mre.vsbds.gui.panel;

import mre.vsbds.core.util.Precondition;
import mre.vsbds.gui.util.Border;
import mre.vsbds.gui.util.Layout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import java.util.Timer;
import java.util.TimerTask;

public final class Control extends JPanel
{
    private final Gallery  gallery;
    private final Controls controls;
    private final Player   player;

    public Control(final Gallery gallery)
    {
        this.gallery  = Precondition.nonNull(gallery);
        this.player   = new Player();
        this.controls = new Controls();

        this.setLayout(Layout.borderLayout());
        this.add(player, Layout.borderLayoutCenter());
        this.add(controls, Layout.borderLayoutSouth());

        gallery.onViewClick(player::play);
    }

    private final class Player extends JLabel
    {
        private final JLabel       label  = new JLabel();
        private final TitledBorder border = Border.titleBorder(" ");

        private Pages.View current   = null;
        private TimerTask  playTask  = null;
        private Timer      playTimer = null;

        private Player()
        {
            this.setLayout(Layout.borderLayout());
            this.add(label, Layout.borderLayoutCenter());
            label.setHorizontalAlignment(0);
        }

        private void play(final Pages.View shot)
        {
            reset();

            current   = Precondition.nonNull(shot);
            playTask  = playShot();
            playTimer = new Timer();

            current.iterator().reset();
            final var fps = (double) gallery.twinComparison().threshold().video().frameRate();
            final var fpm = (fps / 1000.0f);         // frames per milliseconds
            final var mpf = Math.round(1.0d / fpm);  // milliseconds per frame
            playTimer.schedule(playTask, 50, mpf);
        }

        private void reset()
        {
            this.setBorder(border);

            border.setTitle(" ");
            label.setIcon(null);

            if (playTask != null)
                playTask.cancel();

            if (playTimer != null)
                playTimer.cancel();

            playTask  = null;
            playTimer = null;
            current   = null;

            this.revalidate();
            this.repaint();
        }

        private TimerTask playShot()
        {
            return new TimerTask()
            {
                @Override
                public void run()
                {
                    Precondition.nonNull(current != null);
                    if (current.iterator().next().image() == null)
                    {
                        this.cancel();
                        playTimer.cancel();
                        label.setIcon(current.thumbnail());
                    }
                    else
                    {
                        label.setIcon(current.iterator().icon());
                    }
                }
            };
        }
    }

    private final class Controls extends JPanel
    {
    }
}
