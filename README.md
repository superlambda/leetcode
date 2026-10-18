# leetcode

Java solutions and interview practice, organized as a standard Maven project.

## Requirements

- JDK 26+
- Maven 3.10+

## Layout

```text
src/main/java/    Solutions and interview examples
src/test/java/    JUnit 6 tests
python/           Python practice scripts
```

## Build and test

```bash
mvn clean test                                   # compile and run all tests
mvn -Dtest=SolutionTest test                     # run a test class by simple name
mvn -Dtest=peter.liu.twosum.SolutionTest test    # run a test class by FQCN
```

Tests use JUnit 6 (Jupiter API) with Maven Surefire 3.6.0.

## Notes

- Non-Java files under `src/main/java` are packaged as resources by `pom.xml`.
- Project configuration lives in `pom.xml`; no Eclipse/IDE metadata is tracked.
