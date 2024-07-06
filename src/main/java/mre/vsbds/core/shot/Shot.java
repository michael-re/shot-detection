package mre.vsbds.core.shot;

import mre.vsbds.core.util.Precondition;

public abstract class Shot implements Comparable<Shot>
{
    private final int firstFrame;
    private final int lastFrame;

    public Shot(final int firstFrame, final int lastFrame)
    {
        Precondition.validArg(firstFrame >= 0,          "invalid shot start frame");
        Precondition.validArg(lastFrame  >= firstFrame, "invalid shot end frame");

        this.firstFrame = firstFrame;
        this.lastFrame  = lastFrame;
    }

    public final int firstFrame()
    {
        return firstFrame;
    }

    public final int lastFrame()
    {
        return lastFrame;
    }

    @Override
    public final int compareTo(final Shot other)
    {
        final var otherFirstFrame   = (other == null) ? Integer.MAX_VALUE : other.firstFrame;
        final var otherLastFrame    = (other == null) ? Integer.MAX_VALUE : other.lastFrame;
        final var firstFrameCompare = Integer.compare(this.firstFrame, otherFirstFrame);
        return (firstFrameCompare == 0)
             ? Integer.compare(this.lastFrame, otherLastFrame)
             : firstFrameCompare;
    }
}
