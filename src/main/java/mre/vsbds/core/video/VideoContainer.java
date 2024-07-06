package mre.vsbds.core.video;

import mre.vsbds.core.util.Nullable;
import mre.vsbds.core.util.Precondition;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;
import java.io.File;

public final class VideoContainer
{
    private final File                 file;
    private final FFmpegFrameGrabber   grabber;
    private final Java2DFrameConverter converter;

    private VideoContainer(final File file, final FFmpegFrameGrabber grabber)
    {
        this.file      = Precondition.nonNull(file);
        this.grabber   = Precondition.nonNull(grabber);
        this.converter = new Java2DFrameConverter();
    }

    public static VideoContainer read(final File file)
    {
        if (file == null || !file.isFile() || !file.exists() || !file.canRead())
            return null;

        final var grabber = Nullable.value(() -> {
            final var g = new FFmpegFrameGrabber(file);
            g.start();
            return g;
        });

        return (grabber == null)
             ? null
             : new VideoContainer(file, grabber);
    }

    public static VideoContainer read(final String path)
    {
        return read(Nullable.value(() -> new File(path)));
    }

    public File file()
    {
        return file;
    }

    public int frameCount()
    {
        return grabber.getLengthInVideoFrames();
    }

    public VideoFrameIterator iterator()
    {
        return iterator(0, frameCount() - 1);
    }

    public VideoFrameIterator iterator(final int beg, final int end)
    {
        Precondition.validArg(beg >= 0,   "invalid iterator start index");
        Precondition.validArg(beg <= end, "invalid iterator range");
        return new VideoFrameIterator(this, beg, end);
    }

    public synchronized BufferedImage frame(final int frameNumber)
    {
        try
        {
            grabber.setVideoFrameNumber(frameNumber);
            final var frame = grabber.grabImage();
            return (frame == null) ? null : converter.convert(frame);
        }
        catch (Exception _)
        {
            return null;
        }
    }
}
