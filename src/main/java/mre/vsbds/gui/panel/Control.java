package mre.vsbds.gui.panel;

import mre.vsbds.core.util.Precondition;
import mre.vsbds.gui.util.Assets;
import mre.vsbds.gui.util.Border;
import mre.vsbds.gui.util.Layout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public final class Control extends JPanel
{
    private final Gallery  gallery;
    private final Player   player;

    public Control(final Gallery gallery)
    {
        this.gallery = Precondition.nonNull(gallery);
        this.player  = new Player();

        this.setLayout(Layout.borderLayout());
        this.add(player, Layout.borderLayoutCenter());

        final var controls = new Controls();
        this.add(controls, Layout.borderLayoutSouth());

        gallery.onViewClick(player::select);
    }

    private final class Player extends JLabel
    {
        private final JLabel label     = new JLabel();
        private Pages.View   current   = null;
        private TimerTask    playTask  = null;
        private Timer        playTimer = null;

        private Player()
        {
            this.setLayout(Layout.borderLayout());
            this.add(label, Layout.borderLayoutCenter());
            this.setBorder(Border.titleBorder(" "));
            label.setHorizontalAlignment(0);
        }

        private void select(final Pages.View shot)
        {
            reset();
            current = shot;
            label.setIcon(shot.thumbnail());
        }

        private void play(final Pages.View shot)
        {
            reset();
            if (shot != null)
            {
                current   = shot;
                playTask  = playShot();
                playTimer = new Timer();

                current.iterator().reset();
                final var fps = (double) gallery.twinComparison().threshold().video().frameRate();
                final var fpm = (fps / 1000.0f);         // frames per milliseconds
                final var mpf = Math.round(1.0d / fpm);  // milliseconds per frame
                playTimer.schedule(playTask, 0, mpf);
            }
        }

        private void reset()
        {
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
                        label.setIcon(current.iterator().largeIcon());
                    }
                }
            };
        }
    }

    private final class Controls extends JPanel
    {
        private final Map<String, JButton> buttons = new HashMap<>();

        private Controls()
        {
            this.removeAll();

            this.setLayout(Layout.flowLayout());
            this.setBorder(Border.emptyBorder());

            addButton("play",  "play")       .addActionListener(_ -> play());
            addButton("clear", "clear")      .addActionListener(_ -> reset());
            addButton("prev",  "arrow-left") .addActionListener(_ -> gallery.showPrevPage());
            addButton("next",  "arrow-right").addActionListener(_ -> gallery.showNextPage());

            buttons.get("prev").setHorizontalTextPosition(JButton.TRAILING);
            buttons.get("next").setHorizontalTextPosition(JButton.LEADING);

            buttons.get("prev").setText("prev page");
            buttons.get("next").setText("next page");

            this.revalidate();
            this.repaint();
        }

        private void play()
        {
            if (player.current == null)
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Please select a shot to play",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            player.play(player.current);
        }

        private void reset()
        {
            gallery.reset();
            player.reset();
        }

        private JButton addButton(final String name, final String icon)
        {
            if (!buttons.containsKey(name))
            {
                buttons.put(name, new JButton(name));
                buttons.get(name).setIcon(Assets.icon(icon));
                buttons.get(name).setFocusable(false);
            }

            this.add(buttons.get(name));
            return buttons.get(name);
        }
    }
}
