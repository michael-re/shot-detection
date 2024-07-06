package mre.vsbds.gui;

import mre.vsbds.gui.panel.Menu;
import mre.vsbds.gui.util.Assets;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public final class Window extends JFrame
{
    private final Menu menu;

    public Window()
    {
        this.menu = new Menu();

        this.initFrame();
        this.initMenu();
    }

    private void initFrame()
    {
        this.setTitle("Video Shot Boundary Detection System");
        this.setSize(1_000, 1_000);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initMenu()
    {
        menu.addItem("File", "Exit").setIcon(Assets.icon("exit"));
        menu.getItem("Exit")        .addActionListener(_ -> exitAction());
        this.setJMenuBar(menu);
    }

    private void exitAction()
    {
        this.dispose();
        System.exit(0);
    }
}
