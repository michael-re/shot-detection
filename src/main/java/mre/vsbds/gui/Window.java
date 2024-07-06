package mre.vsbds.gui;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public final class Window extends JFrame
{
    public Window()
    {
        this.initFrame();
    }

    private void initFrame()
    {
        this.setTitle("Video Shot Boundary Detection System");
        this.setSize(1_000, 1_000);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
