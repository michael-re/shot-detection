package mre.vsbds.core.video;

import java.awt.image.BufferedImage;

public final class VideoFrameBuffer
{
    protected BufferedImage image       = null;
    protected int           index       = -1;
    protected int           frameNumber = -1;

    public BufferedImage image()
    {
        return image;
    }

    public int index()
    {
        return index;
    }

    public int frameNumber()
    {
        return frameNumber;
    }
}
