# Source code

[Instroduction](../readme.md)
| **Source**
| [Features](features.md)
| [User manual](manual.md)
| [Scripting](scripting.md)
| [F.A.Q.](faq.md)

## Checkout source code

`git clone https://github.com/jiaqi/jmxterm.git`

## Build with Maven

The Jmxterm project is built with Maven, so there is no Makefile or
`build.xml` in the source repository. To build artifacts from the
source, make sure Maven is installed on your computer and run the
following command:

```
mvn clean package
```

## Build RPM

Codehaus Mojos released a Maven RPM plugin for RPM generation. This
plugin solves one of the open questions nicely. With it, an RPM
package can be generated from the source code using a Maven command:

```
mvn rpm:rpm
```

The Maven RPM plugin was added recently, so I have not released an RPM
artifact on the SourceForge download page. In the next release of
Jmxterm there will be a downloadable RPM file. Until then, you need to
build from source if you want an RPM file.

DEB build is on the way but not ready yet, unfortunately.
