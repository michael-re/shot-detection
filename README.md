# Video Shot Boundary Detection System

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

4. Run:

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
