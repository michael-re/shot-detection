package mre.vsbds;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import mre.vsbds.gui.Window;

import javax.swing.SwingUtilities;

public final class Main
{
    public static void main(final String[] args)
    {
        FlatMacLightLaf.setup();
        SwingUtilities.invokeLater(() ->
        {
            final var window = new Window();
            window.setVisible(true);
        });
    }
}
