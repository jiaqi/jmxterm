# JMXTerm - Copilot Development Instructions

## Project Overview

JMXTerm is an interactive command-line based JMX (Java Management Extensions) client that allows users to connect to JMX-enabled Java applications for monitoring and management. This tool provides a terminal interface for browsing MBeans, executing operations, and monitoring attributes.

**Repository Size**: ~200 Java source files, 8MB uber JAR  
**Languages**: Java 17, Maven build system, Shell scripts  
**Target Runtime**: JDK 17+ (works with JDK 21)  
**Main Entry Point**: `org.cyclopsgroup.jmxterm.boot.CliMain`

## Build and Validation Commands

### Prerequisites
- **Java 17 or higher** (JDK 21 supported)
- **Maven 3.6+** (uses Maven wrapper)
- No additional tools required - all dependencies managed via Maven

### Essential Build Commands

**ALWAYS run commands in this specific order:**

1. **Clean**: `mvn -B -q --no-transfer-progress clean`
2. **Compile**: `mvn -B -q --no-transfer-progress compile` 
3. **Test**: `mvn -B -q --no-transfer-progress test`
4. **Verify**: `mvn -B -q --no-transfer-progress verify` (includes integration tests)
5. **Package**: `mvn -B -q --no-transfer-progress package` (creates uber JAR)

### Key Build Artifacts
- **Main JAR**: `target/jmxterm-${version}.jar` (library only)
- **Uber JAR**: `target/jmxterm-${version}-uber.jar` (executable with dependencies)
- **Debian Package**: `target/jmxterm_${version}_all.deb`
- **Sources JAR**: `target/jmxterm-${version}-sources.jar`

### Running the Application
```bash
# Interactive mode
java -jar target/jmxterm-*-uber.jar

# Non-interactive with script file
java -jar target/jmxterm-*-uber.jar -n -i script.txt

# Help
java -jar target/jmxterm-*-uber.jar --help
```

### Common Build Issues & Workarounds

**Issue**: Maven compile failures with "package does not exist"  
**Solution**: Always run `mvn clean` before `mvn compile` when switching branches

**Issue**: Test failures on different JDK versions  
**Solution**: Tests are configured to exclude JDK-specific tests (`org.cyclopsgroup.jmxterm.jdk*`)

**Issue**: Docker build fails  
**Solution**: Ensure uber JAR exists: `mvn package` must complete successfully first

**Timing**: Full build including tests takes ~30-60 seconds. Package step adds ~10 seconds for uber JAR creation.

## Project Architecture & Key Files

### Core Architecture
```
src/main/java/org/cyclopsgroup/jmxterm/
├── boot/              # Application bootstrap (CliMain)
├── cmd/              # Command implementations (15+ commands)
├── cc/               # Command center and session management  
├── io/               # Input/output handling (file, console, streams)
├── pm/               # Process management for JVM discovery
├── utils/            # Utility classes
├── jdk5/            # JDK 5 compatibility layer
├── jdk6/            # JDK 6 specific implementations  
└── jdk9/            # JDK 9+ specific implementations
```

### Critical Files
- **Main Class**: `src/main/java/org/cyclopsgroup/jmxterm/boot/CliMain.java`
- **Base Command**: `src/main/java/org/cyclopsgroup/jmxterm/Command.java`
- **Command Center**: `src/main/java/org/cyclopsgroup/jmxterm/cc/CommandCenter.java`
- **Build Config**: `pom.xml` (Maven configuration)
- **Shell Script**: `src/main/script/jmxterm.sh` (system installation)

### Configuration Files
- **Maven**: `pom.xml` (dependencies, plugins, build configuration)
- **CI/CD**: `.github/workflows/maven.yaml` (GitHub Actions)
- **Docker**: `Dockerfile` (containerization)
- **Packaging**: `src/main/deb/` (Debian packaging)

### Available Commands (in src/main/java/org/cyclopsgroup/jmxterm/cmd/)
- `OpenCommand` - Connect to JMX server
- `CloseCommand` - Disconnect from server
- `DomainsCommand` - List MBean domains
- `BeansCommand` - List MBeans in domain
- `InfoCommand` - Show MBean information
- `GetCommand` - Get attribute values
- `SetCommand` - Set attribute values
- `RunCommand` - Execute MBean operations
- `WatchCommand` - Monitor attribute changes
- `SubscribeCommand` - Subscribe to notifications

## CI/CD & Validation Pipeline

### GitHub Actions Workflow (`.github/workflows/maven.yaml`)
1. **Test Job**: Runs `mvn verify` on JDK 17 & 21
2. **Build Job**: Creates packages, runs Trivy security scan
3. **Docker Job**: Builds multi-arch Docker images

### Validation Steps to Replicate CI
```bash
# Full validation (matches CI)
mvn -B -q --no-transfer-progress verify

# Security scanning (if Trivy installed)
trivy fs . --format sarif --output trivy-results.sarif
```

### Pre-commit Checks
No specific pre-commit hooks, but always ensure:
1. `mvn verify` passes completely
2. No new compiler warnings
3. All tests pass
4. Uber JAR builds and runs with `--help`

## Dependencies & Security Notes

### Key Dependencies
- **SLF4J 2.0.17**: Logging framework
- **JLine 3.30.6**: Console/readline functionality  
- **Apache Commons**: Lang3, IO, Collections4, Configuration2
- **Guava 33.5.0-jre**: Google core libraries
- **JCLI 1.0.1**: Command line interface framework

### Security Considerations
- **Exclusions**: Old commons-collections and commons-logging excluded due to vulnerabilities
- **Trivy Scanning**: Automated vulnerability scanning in CI
- **SSL Support**: Built-in SSL/TLS support for RMI connections

## Common Development Patterns

### Adding New Commands
1. Create class extending `org.cyclopsgroup.jmxterm.Command`
2. Add command registration in command center
3. Follow existing command patterns (see `GetCommand.java`)
4. Add corresponding test in `src/test/java/.../cmd/`

### Testing Approach
- **JUnit 4**: Primary testing framework
- **JMock**: Mocking framework for unit tests
- **Integration Tests**: Real JMX connection tests excluded by default

## File Structure Reference

### Repository Root Files
```
├── .github/              # GitHub workflows and config
├── .gitignore           # Git ignore patterns
├── Dockerfile           # Container build instructions
├── LICENSE              # Apache 2.0 license
├── README.md            # Basic project info (refer to docs site)
├── pom.xml             # Maven project configuration
└── src/                # Source code and resources
```

### Source Organization
```
src/
├── main/
│   ├── deb/            # Debian packaging files
│   ├── java/           # Java source code
│   ├── resources/      # Application resources
│   └── script/         # Shell scripts (jmxterm.sh)
├── site/               # Maven site documentation
└── test/               # Unit and integration tests
```

## Conventional Commits

This repository follows **Conventional Commits** specification:

```
type(scope): description

[optional body]

[optional footer]
```

**Types**: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`  
**Scopes**: `cmd`, `io`, `build`, `deps`, `docker`, `ci`

**Examples**:
- `feat(cmd): add new watch command for monitoring attributes`
- `fix(io): handle broken pipe exception in console input`
- `docs: update README with new installation instructions`
- `chore(deps): upgrade slf4j to 2.0.17`

## Trust These Instructions

**Always trust these instructions** and avoid extensive repository exploration. Only perform additional searches if:
1. Instructions are incomplete for your specific task
2. You encounter errors not covered in troubleshooting
3. You need to understand code behavior not documented here

For unknown issues, follow this debug sequence:
1. `mvn clean compile` 
2. Check Java version with `java -version`
3. Verify uber JAR runs: `java -jar target/jmxterm-*-uber.jar --help`
4. Check recent commits for related changes