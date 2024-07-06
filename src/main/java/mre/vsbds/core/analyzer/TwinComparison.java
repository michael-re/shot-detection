package mre.vsbds.core.analyzer;

import mre.vsbds.core.shot.CutShot;
import mre.vsbds.core.shot.Shot;
import mre.vsbds.core.shot.TransitionShot;
import mre.vsbds.core.util.Precondition;
import mre.vsbds.core.video.Video;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TwinComparison
{
    private final Threshold  threshold;
    private final List<Shot> allShots;
    private final List<Shot> cutShots;
    private final List<Shot> transitionShots;

    public TwinComparison(final Video video, final int frameBeg, final int frameEnd)
    {
        Precondition.nonNull(video);
        Precondition.validArg(frameBeg >= 0,                      "invalid start frame");
        Precondition.validArg(frameBeg <= frameEnd,               "invalid frame range");
        Precondition.validArg(frameEnd <= video.frameCount() - 1, "invalid end frame");

        this.threshold       = new Threshold(video, frameBeg, frameEnd);
        this.allShots        = new ArrayList<>();
        this.cutShots        = new ArrayList<>();
        this.transitionShots = new ArrayList<>();

        detectShots();
        System.out.println(threshold);

        System.out.println("Cut Shots ================");
        cutShots.forEach(shot -> System.out.println("| " + shot));
        System.out.println("+=========================\n");

        System.out.println("Transition Shots =========");
        transitionShots.forEach(shot -> System.out.println("| " + shot));
        System.out.println("+=========================\n");
    }

    public Threshold threshold()
    {
        return threshold;
    }

    public List<Shot> allShots()
    {
        return Collections.unmodifiableList(allShots);
    }

    public List<Shot> cutShots()
    {
        return Collections.unmodifiableList(cutShots);
    }

    public List<Shot> transitionShots()
    {
        return Collections.unmodifiableList(transitionShots);
    }

    private void detectShots()
    {
        for (var startFrame = 0; startFrame < threshold.sdLength(); startFrame++)
        {
            if (aboveCutShotThreshold(startFrame))
            {
                startFrame = addCutShot(startFrame, startFrame + 1);
            }
            else if (aboveTransitionShotThreshold(startFrame))
            {
                final var candidate = transitionShotEndFrameCandidate(startFrame);
                final var endFrame  = addTransitionShot(startFrame, candidate);
                startFrame          = Math.max(startFrame, endFrame);
            }
        }
    }

    private boolean aboveCutShotThreshold(final int frame)
    {
        return threshold.sd(frame) >= threshold.tb();
    }

    private int addCutShot(final int cs, final int ce)
    {
        final var frameBeg = cs + threshold.frameBeg();
        final var frameEnd = ce + threshold.frameBeg();
        final var shot     = new CutShot(frameBeg, frameEnd);

        cutShots.add(shot);
        allShots.add(shot);

        return ce;
    }

    private boolean aboveTransitionShotThreshold(final int frame)
    {
        return threshold.sd(frame) >= threshold.ts()
            && threshold.sd(frame) <  threshold.tb();
    }

    private int transitionShotEndFrameCandidate(final int startFrame)
    {
        var tor = 0;
        for (var endFrame = startFrame + 1; endFrame < threshold.sdLength(); endFrame++)
        {
            // above transition threshold
            if (aboveTransitionShotThreshold(endFrame))
            {
                tor = 0;
            }

            // below transition shot threshold
            else if (threshold.sd(endFrame) < threshold.ts())
            {
                if ((++tor) == threshold.tor())
                    return endFrame - 2;
            }

            // at transition shot threshold
            else if (threshold.sd(endFrame) >= threshold.tb())
            {
                return endFrame - 1;
            }
        }

        return -1;
    }

    private int addTransitionShot(final int fs, final int fe)
    {
        if ((fs < fe) && (threshold.sum(fs, fe + 1) >= threshold.tb()))
        {
            final var frameBeg = fs + threshold.frameBeg();
            final var frameEnd = fe + threshold.frameBeg();
            final var shot     = new TransitionShot(frameBeg, frameEnd);

            allShots.add(shot);
            transitionShots.add(shot);

            return fe;
        }

        return -1;
    }
}
