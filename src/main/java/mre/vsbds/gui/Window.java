package mre.vsbds.gui;

import mre.vsbds.core.analyzer.TwinComparison;
import mre.vsbds.core.util.Precondition;
import mre.vsbds.gui.panel.Control;
import mre.vsbds.gui.panel.Gallery;
import mre.vsbds.gui.panel.Menu;
import mre.vsbds.gui.util.Assets;
import mre.vsbds.gui.util.Layout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public final class Window extends JFrame
{
    private final Menu    menu;
    private final Control control;
    private final Gallery gallery;

    public Window(final TwinComparison twinComparison)
    {
        this.menu    = new Menu();
        this.gallery = new Gallery(Precondition.nonNull(twinComparison));
        this.control = new Control(gallery);

        this.initFrame();
        this.initMenu();
        this.initPanels();
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

    private void initPanels()
    {
        this.setLayout(Layout.migLayout(10, 10));
        this.getContentPane().add(control, "cell 0 0 10 5");
        this.getContentPane().add(gallery, "cell 0 5 10 5");
    }

    private void exitAction()
    {
        this.dispose();
        System.exit(0);
    }
}
