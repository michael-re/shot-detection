package mre.vsbds;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import mre.vsbds.core.analyzer.TwinComparison;
import mre.vsbds.core.util.Nullable;
import mre.vsbds.core.video.Video;
import mre.vsbds.gui.Window;

import javax.swing.SwingUtilities;
import java.io.File;

public final class Main
{
    public static void main(final String[] args)
    {
        if (args.length != 3)
        {
            System.out.println("usage: java -jar ./vsbds.jar <video-file> <start-frame> <end-frame>");
            System.exit(0);
        }

        final var file     = Nullable.value(() -> new File(args[0]));
        final var frameBeg = Nullable.value(() -> Integer.parseInt(args[1]));
        final var frameEnd = Nullable.value(() -> Integer.parseInt(args[2]));

        if (file == null || !file.exists() || !file.isFile() || !file.canRead())
        {
            System.out.println("error arg: invalid video file path");
            System.exit(0);
        }

        if (frameBeg == null || frameBeg <= 0)
        {
            System.out.println("error arg: invalid start frame");
            System.exit(0);
        }

        if (frameEnd == null || frameEnd <= frameBeg)
        {
            System.out.println("error arg: invalid end frame");
            System.exit(0);
        }

        final var video = Video.read(file);
        if (video == null)
        {
            System.out.println("error: failed to read video file");
            System.exit(0);
        }

        if (frameEnd > video.frameCount())
        {
            System.out.println("error: invalid frame range for video");
            System.exit(0);
        }

        FlatMacLightLaf.setup();
        SwingUtilities.invokeLater(() ->
        {
            final var twinComparison = new TwinComparison(video, frameBeg - 1, frameEnd - 1);
            final var window         = new Window(twinComparison);
            window.setVisible(true);
        });
    }
}
