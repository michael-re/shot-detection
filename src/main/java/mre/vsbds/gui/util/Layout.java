package mre.vsbds.gui.util;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

public final class Layout
{
    private Layout()
    {
    }

    public static BorderLayout borderLayout()
    {
        return new BorderLayout();
    }

    public static String borderLayoutCenter()
    {
        return BorderLayout.CENTER;
    }

    public static String borderLayoutSouth()
    {
        return BorderLayout.SOUTH;
    }

    public static LayoutManager flowLayout()
    {
        return new FlowLayout(FlowLayout.CENTER);
    }

    public static GridBagLayout gridBagLayout()
    {
        return new GridBagLayout();
    }

    public static MigLayout migLayout(final int rows, final int columns)
    {
        return new MigLayout("fill,center,hidemode 3",
                             "[grow,fill]".repeat(rows),
                             "[grow,fill]".repeat(columns));
    }
}
