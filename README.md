# Video Shot Boundary Detection System

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
