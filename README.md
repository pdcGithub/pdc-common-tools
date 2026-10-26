### Description of the repository (pdc-common-tools)
<p>This is a toolkit that I developed myself using Java 8. </p>
<p>The program has been uploaded to the Maven Central repository.</p>
<p>The API description of the toolkit has been updated to my blog site, please check it out for yourself. I recommend that you check the javadoc documentation as I can't demonstrate every API function.</p>

> In order to reduce interference between functions and facilitate unit testing, I have refactored this project. Currently, the project is refactored into three parts: core, database, web.

> In terms of project management, I use Maven multi-module projects. A Maven multi-module project is a structure that splits a large application into multiple logically independent but collaborative modules, usually managed by a parent project (packaging=pom) that oversees multiple sub-modules. Its advantages include code reuse, separation of responsibilities, unified dependency management, and improved build efficiency.
----------------------------------------------------

### repository config

> For version 1.1 and earlier, here is a complete jar package.
_maven:_
```xml
<dependency>
    <groupId>io.github.pdcgithub</groupId>
    <artifactId>pdc-common-tool</artifactId>
    <version>1.1.4-jre8-SNAPSHOT</version>
</dependency>
```

> For version 1.2 and later, it has been refactored here into multiple independent jar packages according to functionality.
_maven:_
```xml
<!-- This is the basic toolkit -->
<dependency>
    <groupId>io.github.pdcgithub</groupId>
    <artifactId>pdc-common-core</artifactId>
    <version>1.2.0-jre8</version>
</dependency>
<!-- This is a toolkit related to databases -->
<!-- It has already defined the core dependency internally, so there is no need to repeatedly write the core dependency information in the pom. -->
<dependency>
    <groupId>io.github.pdcgithub</groupId>
    <artifactId>pdc-common-database</artifactId>
    <version>1.2.0-jre8</version>
</dependency>
<!-- This is a toolkit related to web functionality -->
<!-- It has already defined the core dependency internally, so there is no need to repeatedly write the core dependency information in the pom. -->
<dependency>
    <groupId>io.github.pdcgithub</groupId>
    <artifactId>pdc-common-web</artifactId>
    <version>1.2.0-jre8</version>
</dependency>
```


### for local packing
If you are interested in using this toolkit but the official version updates too slowly, you can try packaging it locally yourself. It is a snapshot version and has not been fully tested. (Note, you need to install Maven first which is used for packaging Java programs.)
<h4>1. Clear the existing content, recompile, and perform unit testing.</h4>

```bash
mvn clean
```
```bash
mvn compile
```
```bash
mvn test
```
Of course, you also can set all the commands at once.
```bash
mvn clean compile test
```
If you want to run specific unit tests, you can handle them like this:
```bash
mvn -Dtest=net.mickarea.tools.utils.test.ConcurrencyTestUtilTester test
```
<h4>2. Package and place it into the local Maven repository</h4>

```bash
mvn install
```
If you just want to package it and don't want to install it into the local Maven repository, you can handle it this way.
```bash
mvn package
```
Usually, I’ll clear it first and then pack it.
```bash
mvn clean package
```

<h4>3. Starting from version 1.2.0, I split the project into multiple submodules. Now it is possible to package only some submodules. In the example below, we only package the submodule core:</h4>

```bash
mvn clean package -pl core -am
```

If you need to install it to the local repository, you can write it like this:

```bash
mvn clean install -pl core -am
```

> `-pl` is used to specify the submodules to build

> `-am` is used to build all modules that this module depends on at the same time

----------------------------------------------------

### release notes
<h4>v 1.2.0 2026-10-15</h4>
<p>1. Refactor the project from a single jar package into multiple optional submodules. For example: core, database, web, etc.</p>
<p>2. Publish the package to the central repository on October 16, 2026.</p>

<h4>v 1.1.4  2026-10-11 (This version has not been published to the central repository)</h4>
<p>1. Fixed the issue with CodeLocationConverterConfig being inaccurate in locating code execution positions.</p>
<p>2. Extend the functionality of the FileNameFilter class.</p>
<p>3. Mark the class TmpDataGramAnalyzer as deprecated.</p>
<p>4. Delete some useless function verification code.</p>
<p>5. Add two functions for file searching in the class FileUtil.</p>
<p>6. Add a utility class ClassUtil.</p>
<p>7. Remove the third-party dependency library reflections</p>

<h4>v 1.1.3  2026-09-24 (This version has not been published to the central repository)</h4>
<p>1. Added a unit test utility class ConcurrencyTestUtil. It's mainly used to check if the functional code has any thread safety issues under high concurrency.</p>
<p>2. Added a custom output parameter converter class CodeLocationConverterConfig. It is used to record code location information when logging.</p>
<p>3. Added a utility class AnnotationUtil for annotation usage. It is mainly used to simplify the handling and recognition of annotations.</p>
<p>4. Added a utility class PropertiesUtil. It is mainly used to read information from properties files.</p>
<p>5. Deprecated the use of SimpleDateFormat in the TimeUtil class and fixed some spelling issues.</p>
<p>6. Deprecated the MyAnnotationScanner class. Its original functionality is replaced by AnnotationUtil.</p>
<p>7. Fixed hidden program defects in the unit test code of the ConcurrencyTestUtil class.</p>
<p>8. Fixed the issue in the RandomUtil class where random sequence values were duplicated during multithreaded execution due to conflicting random seeds.</p>
<p>9. Fixed the issue in the RandomUtil class where non-numeric characters occasionally appeared when generating random numeric strings.</p>
<p>10. Merged version information and pom file contents from the snapshot, release, and main branches in the repository, and deprecated the 1.0.x naming convention.</p>
<p>11. Disabled the gpg verification configuration in the pom file. This is mainly used when publishing jars to the central repository and is not needed for regular use.</p>
<p>12. Deleted some unused test code files.</p>

<h4>v 1.1.0  2025-12-11</h4>
<p>1. For code testing, the JUnit 5 framework was used.</p>
<p>2. Added a static string convertor named StrToDataConvertor. It can convert strings to any data type. If the default implementation does not meet your needs, you can extend it yourself.</p>
<p>3. For the StrUtil class, added functions getJavaBeanFieldsInfo and getModifierInfo.</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;Modified the implementation of the function makeHumpString and added a new function with the same name.</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;Added the function autoConvert. It is mainly responsible for dynamically calling methods in StrToDataConvertor to perform data conversion.</p>
<p>4. For the TimeUtil class, added some time conversion handling, mainly converting strings to LocalDate and LocalDateTime. These are thread-safe.</p>
<p>5. For database handling, added an new database type DBTYPE_MYSQL_ANSI. Unlike DBTYPE_MYSQL, it only handles single quotes.</p>
<p>6. For the SummaryUtil class, solved the memory overflow issue when extracting data summaries from large files (e.g., 4GB or larger).</p>
<p>7. For the database driver, upgraded Microsoft's MS SQLServer JDBC driver package to version 13.2.1.jre8 to resolve some security issues.</p>
<p>8. All old test code from previous versions was moved to /src/test/java. This applies to both main function tests and unit tests of JUnit.</p>
<p>9. For the RSAUtil class, added an new function genKeys with a configurable key length.</p>

<h4>v 1.0.1  2024-12-17</h4>
<p>1. about the class FileUtil, add 2 functions to get the byte information of a file from the file path.</p>
<p>2. about the class SummaryUtil, add funtions to extract summary information from file.</p>
<p>3. about the class TimeUtil, modified the implementation of the function 'getTimestampByLocalDateTime'.</p>
<p>4. about the class EmailUtil, modified the implementation of some functions and add function of mass mailing.</p>
<p>5. about the class PatternUtil, add function 'genRegStrForHtmlTagReplace' to generate regular strings.</p>
<p>6. about the class StrUtil, modify the implementation of function 'injectionTranslateForHtmlEditor'.</p>
<p>7. about the class DBStaticUtil, improve the implementation of function 'loadEntityListBySimpleDBData' and some conversion functions.</p>
<p>8. upgrade oracle jdbc driver to 'ojdbc8' and fix some bugs.</p>
<p>9. fix some typos.</p>

<h4>v 1.0.0  2024-11-01</h4>
<p>1. the first version.</p>