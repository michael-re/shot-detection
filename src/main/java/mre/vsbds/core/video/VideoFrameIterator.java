package mre.vsbds.core.video;

import mre.vsbds.core.util.Precondition;
import java.util.Iterator;

public final class VideoFrameIterator implements Iterator<VideoFrameBuffer>, Iterable<VideoFrameBuffer>
{
    private final VideoContainer   vid;
    private final VideoFrameBuffer buf;
    private final int              beg;
    private final int              end;

    protected VideoFrameIterator(final VideoContainer vid,
                                 final int            beg,
                                 final int            end)
    {
        Precondition.nonNull(vid);
        Precondition.validArg(beg >= 0,   "invalid iterator begin range");
        Precondition.validArg(end >= 0,   "invalid iterator end range");
        Precondition.validArg(beg <= end, "invalid iterator range");

        this.vid = Precondition.nonNull(vid);
        this.buf = new VideoFrameBuffer();
        this.beg = beg;
        this.end = end;
    }

    public int range()
    {
        return end - beg;
    }

    @Override
    public Iterator<VideoFrameBuffer> iterator()
    {
        return this;
    }

    @Override
    public boolean hasNext()
    {
        return buf.frameNumber < end;
    }

    @Override
    public VideoFrameBuffer next()
    {
        if (hasNext())
        {
            buf.frameNumber++;
            buf.index++;
            buf.image = vid.frame(buf.frameNumber);
        }
        else
        {
            buf.frameNumber = Integer.MAX_VALUE;
            buf.index       = Integer.MAX_VALUE;
            buf.image       = null;
        }

        return buf;
    }
}
