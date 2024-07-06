package mre.vsbds.core.shot;

import mre.vsbds.core.util.Precondition;

public abstract class Shot
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
}
