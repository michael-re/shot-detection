# Video Shot Boundary Detection System

>
> ## Table of Contents
>
> - [**Getting Started**](#getting-started)
>   - [Prerequisites](#prerequisites)
>   - [Building \& Running](#building--running)
> - [**Background: Twin-Comparison Detection**](#background-twin-comparison-detection)
>   - [Thresholding](#thresholding)
>   - [Cut Shots](#cut-shots)
>   - [Transition Shots](#transition-shots)
>

## Description

This repository contains my implementation of a video shot boundary detection
system that uses the twin-comparison algorithm to detect both abrupt cuts
and gradual transitions in video sequences. The twin-comparison method
described in the paper [**_`Automatic Partitioning of Full-Motion Video`_**](https://doi.org/10.1007/BF01210504)
is employed for detecting video shot boundaries.

---

## Getting Started

### Prerequisites

Before you begin, ensure you have the following tools installed on your system:

- [`maven`](https://maven.apache.org/)
- [`jdk 22`](https://openjdk.org/)

### Building & Running

Follow the steps below to compile and run the program:

1. Clone repository:

    ```bash
    git clone https://github.com/michael-re/shot-detection.git
    ```

2. Navigate to project directory:

    ```bash
    cd shot-detection/
    ```

3. Build:

    ```bash
    # build
    mvn clean package

    # clean up build artifacts
    mvn clean
    ```

> [!NOTE]
> The build process can take a little while to complete. For example, on my
> machine, it may take approximately **`70 seconds`** to compile the project fully.

1. Run:

    ```bash
    # command format
    java -jar target/vsbds.jar <video-file> <start-frame> <end-frame>

    # example using test video under the `video/` directory
    java -jar target/vsbds.jar video/20020924_juve_dk_02a.mpg 1000 5000
    ```

---

## Background: Twin-Comparison Detection

### Shot Detection Overview

Shot detection is the process of identifying boundaries between different scenes
in a video. These boundaries can represent either **cut shots**, where there is
a sudden transition between scenes, or **transition shots**, where the change is
gradual, such as in a dissolve or fade effect. The goal of shot detection is to
accurately partition a video into its constituent shots, which can then be
analyzed or used for other tasks such as editing, indexing, or retrieval.

The twin-comparison method used in this project detects shot boundaries by
analyzing the statistical differences between consecutive frames. This approach
applies a dual-threshold mechanism to classify the type of transition between
frames: **cut shots** and **transition shots**. These thresholds allow the
system to distinguish between sudden changes and gradual scene alterations.

The shot detection process involves several key steps:

1. **Calculate Frame-To-Frame Differences (SD):** Compute the statistical
   differences between all frames in the specified range, determining how much
   each frame differs from its successor based on their histogram values.

2. **Calculate Threshold Values:** Using the frame-to-frame difference obtained
   in step one, compute the threshold values $`TB`$, $`TS`$, and $`ToR`$.

3. **Evaluate Thresholds:** Iterate through the frames, comparing their
   statistical differences to the thresholds ($`TB`$, $`TS`$, and $`ToR`$) to
   classify the type of shot.

4. **Store Detected Shots:** Store the classified shots for further analysis
   or retrieval.

### Thresholding

Thresholding plays a crucial role in shot detection by providing criteria to
determine when a frame transition qualifies as a boundary. The twin-comparison
method relies on three primary thresholds to classify transitions:

1. **Shot Boundary Threshold (TB):** This threshold identifies the likelihood of
   an abrupt cut between scenes. When the statistical difference (SD) between
   consecutive frames exceeds this threshold, a cut shot is detected.

2. **Transition Threshold (TS):** This threshold differentiates gradual
   transitions from noise. It helps to distinguish subtle changes in the video
   from insignificant fluctuations that do not represent a true scene change.

3. **Tolerance Threshold (ToR):** This threshold controls the maximum number of
   frames that can fall below the transition threshold during a gradual shot.
   It allows for a tolerance window that accounts for smooth transitions over
   multiple frames, such as dissolves or fades.

These thresholds are dynamically derived based on the frame-to-frame differences
between consecutive frames within a defined range. This adaptive approach
ensures that the system can be applied to videos of varying content, frame
rates, and resolutions. The thresholds are adjusted to minimize false positives
(e.g., noise detected as a shot boundary).

### Cut Shots

Cut shots are abrupt transitions where one scene directly shifts to another,
without any gradual change. They are detected when the statistical difference
($`SD`$) between two consecutive frames exceeds the shot boundary threshold
($`TB`$). This is mathematically expressed as:

## $`SD(f) \geq TB`$

$`\text{Where:}`$

- $`SD(f)`$ is the statistical difference between two consecutive frames.
- $`TB`$ is the shot boundary threshold.

When this condition is met, a **cut shot** is added to the list of detected shots.

### Transition Shots

Transition shots represent gradual changes between scenes, such as fades or
dissolves. These shots are detected when the statistical difference ($`SD`$)
between consecutive frames falls within the range of the transition threshold
($`TS`$) and the shot boundary threshold ($`TB`$):

## $`TS \leq SD(f) < TB`$

$`\text{Where:}`$

- $`SD(f)`$ is the statistical difference between two consecutive frames.
- $`TB`$ is the shot boundary threshold.
- $`TS`$ is the shot transition threshold.

The end of a transition shot is identified by either exceeding the shot boundary
threshold ($`TB`$), indicating a cut, or falling below the transition threshold
($`TS`$) for a tolerance window ($`ToR`$) of frames. This tolerance window
ensures that gradual transitions over multiple frames are correctly classified
as transition shots.
