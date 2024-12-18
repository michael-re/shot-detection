package mre.vsbd.core.video;

import mre.vsbd.core.util.Precondition;

import java.awt.Image;
import java.awt.image.BufferedImage;

public final class Frame
{
    BufferedImage image  = null;
    int           index  = -1;
    int           number = -1;

    public BufferedImage image()
    {
        return image;
    }

    public int index()
    {
        return index;
    }

    public int number()
    {
        return number;
    }

    public Image scaled(final double maxWidth, final double maxHeight)
    {
        Precondition.validArg(maxWidth  > 0, "invalid scaled width");
        Precondition.validArg(maxHeight > 0, "invalid scaled height");
        if (image == null) return null;

        final var width   = (double) image.getWidth();
        final var height  = (double) image.getHeight();
        final var ratio   = Math.min(maxWidth / width, maxHeight / height);
        final var scaledW = (int) Math.ceil(width  * ratio);
        final var scaledH = (int) Math.ceil(height * ratio);

        return (scaledW > maxWidth || scaledH > maxWidth)
             ? scaled(scaledW, scaledH)
             : image.getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
    }
}
