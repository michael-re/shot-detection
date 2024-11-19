package mre.vsbd.core.video;

import mre.vsbd.core.util.Precondition;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class FrameIterator implements Iterator<Frame>, Iterable<Frame>
{
    private final Map<Integer, Icon> smallIconCache;
    private final Map<Integer, Icon> largeIconCache;
    private final Video              video;
    private final Frame              frame;
    private final int                frameBeg;
    private final int                frameEnd;

    public FrameIterator(final Video video,
                         final int   frameBeg,
                         final int   frameEnd)
    {
        Precondition.nonNull(video);
        Precondition.validArg(frameBeg >= 0,        "invalid iterator begin range");
        Precondition.validArg(frameEnd >= 0,        "invalid iterator end range");
        Precondition.validArg(frameBeg <= frameEnd, "invalid iterator range");

        this.smallIconCache = new HashMap<>();
        this.largeIconCache = new HashMap<>();
        this.video          = video;
        this.frame          = new Frame();
        this.frameBeg       = frameBeg;
        this.frameEnd       = frameEnd;
    }

    public FrameIterator reset()
    {
        frame.index  = -1;
        frame.number = -1;
        frame.image  = null;
        return this;
    }

    public int range()
    {
        return frameEnd - frameBeg;
    }

    public Icon smallIcon()
    {
        if (frame.image() == null || frame.index() == -1)
            return null;

        if (!smallIconCache.containsKey(frame.index))
        {
            final var image = frame.scaled(110, 70);
            final var icon  = new ImageIcon(Precondition.nonNull(image));
            smallIconCache.put(frame.index, icon);
        }

        return smallIconCache.get(frame.index);
    }

    public Icon largeIcon()
    {
        if (frame.image() == null || frame.index() == -1)
            return null;

        if (!largeIconCache.containsKey(frame.index))
        {
            final var image = frame.scaled(700, 325);
            final var icon  = new ImageIcon(Precondition.nonNull(image));
            largeIconCache.put(frame.index, icon);
        }

        return largeIconCache.get(frame.index);
    }

    @Override
    public Iterator<Frame> iterator()
    {
        return this;
    }

    @Override
    public boolean hasNext()
    {
        return frame.number() < frameEnd &&
               frame.number() < video.frameCount();
    }

    @Override
    public Frame next()
    {
        if (hasNext())
        {
            frame.number = (frame.number == -1) ? frameBeg : frame.number + 1;
            frame.index += 1;
            frame.image  = video.frame(frame.number);
        }
        else
        {
            frame.number = Integer.MAX_VALUE;
            frame.index  = Integer.MAX_VALUE;
            frame.image  = null;
        }

        return frame;
    }
}
