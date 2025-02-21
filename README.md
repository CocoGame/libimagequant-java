# libimagequant-java
libimagequant-java is a JNI wrapper around the C API for [libimagequant](https://pngquant.org/lib/), enabling you to quantize 32-bit RGBA images.

libimagequant-java is kept intentionally simple, only operating on `byte[]` arrays and direct `ByteBuffer` instances.

The required native shared library is automatically loaded for your operating system. No messing around with `-Djava.library.path`. libimagequant-java supports Windows 32- & 64-bit, Linux 32- & 64-bit as well as macOS out of the box.

## Installation
本项目修改了JNI代码，并使用pngquant原来的JNI接口，且梳理了编译流程，如需使用自行编译即可.  
如果想直接使用libimagequant-java原始库，可通过以下方式添加依赖:  

```xml
<dependency>
	<groupId>com.badlogicgames</groupId>
	<artifactId>libimagequant-java</artifactId>
	<version>1.2-SNAPSHOT</version>
</dependency>
```

To include it in your Gradle project, ensure your `build.gradle` file adds thd repository:

```
maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
```
Then add the dependency:
```groovy
compile "com.badlogicgames:libimagequant-java:1.2-SNAPSHOT"
```

libimagequant-java is also built on every new commit by [Jenkins](https://libgdx.badlogicgames.com/jenkins/job/libimagequant-java/) and published as a [SNAPSHOT release](https://oss.sonatype.org/content/repositories/snapshots/com/badlogicgames/libimagequant-java/) to SonaType.

## Usage
The below code loads a 32-bit RGBA PNG file, quantizes it to 8-bit and then writes it back out to a new file.

```java
new SharedLibraryLoader().load("imagequant-java");

PngQuant pngQuant = new PngQuant();
pngQuant.setQuality(10);
BufferedImage image = ImageIO.read(new File("test/origin.png"));

BufferedImage remapped = pngQuant.getRemapped(image);
ImageIO.write(remapped, "png", new File("test/1.png "));
```

Refer to the [libimagequant documentation](https://pngquant.org/lib/) for more information.

## Working from source
libimagequant-java includes the upstream libimagequant repository as a submodule in `jni/libimagequant`. Make sure to clone this repository recursively to also check out the submodule:

```
git clone --recursive https://github.com/badlogic/libimagequant-java
```

libimagequant-java is a plain old Maven project and can be imported into any Maven aware IDE. Before importing, run

```
mvn clean compile
```

from a terminal. This will download the latest native shared libraries from [Jenkins](https://libgdx.badlogicgames.com/ci/libimagequant-java/binaries/) so you do not have to build them yourself. After the above Maven invocation, the native shared libraries will be located in `src/main/resources`.

### Building the native libraries
To build the native shared libraries yourself, you can use the included `jni/build-docker.sh` script for Windows 32-bit, Windows 64-bit, Linux 32-bit and Linux 64-bit. Install [Docker CE](https://www.docker.com/community-edition), then in your terminal:

使用Mac OS编译，安装工具：XCode、Command Line Tools、MinGW-w64、Docker；

```bash
cd jni/
./build-all.sh
```

This will build a Docker image with all required toolchains for Windows and Linux, compile the libimagequant-java shared libraries and put them in `src/main/resources`.

Finally, if you want to build a shared library for your specific host, use the `jni/build.sh` script:

```bash
/build.sh --target=<target-os-arch> --build=<release|debug>
```

Valid values for the `--target` parameter to pass to `build.sh` are `macosx`, `windows-x86`, `windows-x86_64`, `linux-64` and `linux-x86_64`.

Valid values for the `--build` parameter are `release` and `debug`.

To build locally, all required toolchains must be installed; Xcode with command line tools on macOS, Mingw64 on Windows (or Linux for cross-compilation) and GCC on Linux.

### Modifying the C++ JNI code
libimagequant-java uses [gdx-jnigen](https://github.com/libgdx/libgdx/wiki/jnigen) to generate C++ code embedded in the Java files. When you modify the embedded C++ code, you have to re-generate the C++ files in `jni/src/` by running the CodeGenerator class.

## License
AGPL 3, See [LICENSE](https://github.com/badlogic/libimagequant-java/blob/master/LICENSE). For alternative licensing options contact the libimagequant [author](https://kornel.ski/contact).
