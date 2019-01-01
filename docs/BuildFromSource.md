## Checkout source code

git clone https://github.com/jiaqi/jmxterm.git
 
### Build with Maven

Jmxterm project is built with [Maven](http://maven.apache.org) so there isn't a Makefile or build.xml in source repository. To build artifacts from source code, make sure Maven is installed on your computer and run following commands:

```
mvn clean package
```

## Build RPM

Codehaus Mojos released a [Maven RPM plugin](http://mojo.codehaus.org/rpm-maven-plugin) for RPM generation, which is really cool and solves one of the open questions perfectly. With this plugin, RPM package can be easily generated from source code with maven command:

```
mvn rpm:rpm
```

The Maven RPM plugin was added recently so I haven't released RPM artifact in sourceforge download page. In next release of Jmxterm, there will be a downloadable RPM file. Until then, you need to build from source if you want RPM file.

DEB build is on the way but not ready yet, unfortunately.