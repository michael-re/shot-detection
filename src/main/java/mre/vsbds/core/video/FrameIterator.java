package mre.vsbds.core.video;

import mre.vsbds.core.util.Precondition;
import java.util.Iterator;

public final class FrameIterator implements Iterator<Frame>, Iterable<Frame>
{
    private final Video video;
    private final Frame frame;
    private final int   frameBeg;
    private final int   frameEnd;

    protected FrameIterator(final Video video,
                            final int   frameBeg,
                            final int   frameEnd)
    {
        Precondition.nonNull(video);
        Precondition.validArg(frameBeg >= 0,        "invalid iterator begin range");
        Precondition.validArg(frameEnd >= 0,        "invalid iterator end range");
        Precondition.validArg(frameBeg <= frameEnd, "invalid iterator range");

        this.video    = video;
        this.frame    = new Frame();
        this.frameBeg = frameBeg;
        this.frameEnd = frameEnd;
    }

    public int range()
    {
        return frameEnd - frameBeg;
    }

    @Override
    public Iterator<Frame> iterator()
    {
        return this;
    }

    @Override
    public boolean hasNext()
    {
        return frame.number() < frameEnd;
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
