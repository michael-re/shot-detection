package mre.vsbds.core.shot;

public final class CutShot extends Shot
{
    public CutShot(final int firstFrame, final int lastFrame)
    {
        super(firstFrame, lastFrame);
    }

    @Override
    public String toString()
    {
        return "cs: " + firstFrame() + ", ce: " + lastFrame();
    }
}
