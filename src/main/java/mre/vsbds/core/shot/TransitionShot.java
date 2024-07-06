package mre.vsbds.core.shot;

public class TransitionShot extends Shot
{
    public TransitionShot(final int firstFrame, final int lastFrame)
    {
        super(firstFrame, lastFrame);
    }

    @Override
    public String toString()
    {
        return "fs: " + firstFrame() + ", fe: " + lastFrame();
    }
}
