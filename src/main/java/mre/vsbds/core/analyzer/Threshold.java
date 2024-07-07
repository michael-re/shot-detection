package mre.vsbds.core.analyzer;

import mre.vsbds.core.util.Precondition;
import mre.vsbds.core.video.Video;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.IntStream;

public final class Threshold
{
    private final Video    video;
    private final int      frameBeg;
    private final int      frameEnd;
    private final double[] sd;
    private final double   mean;
    private final double   std;
    private final double   tb;
    private final double   ts;
    private final int      tor;

    public Threshold(final Video video, final int frameBeg, final int frameEnd)
    {
        System.out.println("\n==== beg: computing threshold values ====");

        this.video    = Precondition.nonNull(video);
        this.frameBeg = frameBeg;
        this.frameEnd = frameEnd;

        Precondition.validArg(frameBeg >= 0,                      "invalid start frame");
        Precondition.validArg(frameBeg <= frameEnd,               "invalid frame range");
        Precondition.validArg(frameEnd <= video.frameCount() - 1, "invalid end frame");

        this.sd   = computeSD();
        this.mean = computeMean();
        this.std  = computeSTD();
        this.tb   = mean + std * 11;
        this.ts   = mean * 2;
        this.tor  = 2;

        System.out.println("==== end: computing threshold values ====\n");
    }

    public Video video()
    {
        return video;
    }

    public int frameBeg()
    {
        return frameBeg;
    }

    public int frameEnd()
    {
        return frameEnd;
    }

    public double sum(final int start, final int end)
    {
        return IntStream.range(start, end).mapToDouble(this::sd).sum();
    }

    public double sd(final int index)
    {
        return (index >= 0) && (index < sd.length)
             ? sd[index]
             : 0.0d;
    }

    public int sdLength()
    {
        return sd.length;
    }

    public double tb()
    {
        return tb;
    }

    public double ts()
    {
        return ts;
    }

    public double tor()
    {
        return tor;
    }

    @Override
    public String toString()
    {
        final var df = new DecimalFormat("000,000.00000");
        return "\nThreshold values ========="
             + "\n| mean: " + df.format(mean)
             + "\n| std:  " + df.format(std)
             + "\n| tb:   " + df.format(tb)
             + "\n| ts:   " + df.format(ts)
             + "\n| tor:  " + df.format(tor)
             + "\n+========================\n";
    }

    private double[] computeSD()
    {
        final var histograms       = extractHistograms();
        final var frameToFrameDiff = new double[histograms.length];
        final var total            = (float) frameToFrameDiff.length;

        for (var frame = 0; frame < frameToFrameDiff.length - 1; frame++)
        {
            for (var bin = 0; bin < histograms[frame].length; bin++)
            {
                final var currFrameBin   = histograms[frame][bin];
                final var nextFrameBin   = histograms[frame + 1][bin];
                frameToFrameDiff[frame] += Math.abs(currFrameBin - nextFrameBin);
            }

            final var progress = Math.round((frame / total) * 100.0f);
            System.out.print("\033[2K\r- computing f-to-f diff: " + progress + "%");
        }

        System.out.println("\033[2K\r- computing f-to-f diff: 100% (done)");
        return frameToFrameDiff;
    }

    private double computeMean()
    {
        return Arrays.stream(sd).sum() / ((double) sd.length);
    }

    private double computeSTD()
    {
        final var sum = Arrays.stream(sd).map(x -> Math.pow(x - mean, 2)).sum();
        return Math.sqrt(sum / ((double) sd.length - 1));
    }

    private double[][] extractHistograms()
    {
        final var iterator   = video.iterator(frameBeg, frameEnd);
        final var histograms = new double[iterator.range() + 1][25];
        final var total      = (float) histograms.length;

        for (final var view : iterator)
        {
            final var image = Precondition.nonNull(view.image());
            final var index = view.index();
            final var bins  = histograms[index];

            for (var y = 0; y < image.getHeight(); y++)
            {
                for (var x = 0; x < image.getWidth(); x++)
                {
                    final var c = image.getRGB(x, y);
                    final var r = ((c >> 16) & 0xff) * 0.299f;
                    final var g = ((c >> 8)  & 0xff) * 0.587f;
                    final var b = ((c)       & 0xff) * 0.114f;
                    final var i = Math.min(240.0f, r + g + b) / 10.0f;
                    bins[(int) i]++;
                }
            }

            final var progress = Math.round((index / total) * 100.0f);
            System.out.print("\033[2K\r- extracting histograms: " + progress + "%");
        }

        System.out.println("\033[2K\r- extracting histograms: 100% (done)");
        return histograms;
    }
}
