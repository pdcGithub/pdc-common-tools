### 版本库(pdc-common-tools)说明
<p>这是一个我自己使用 Java 8 开发的工具包。</p>
<p>程序第一个初始版本，已上传到 Maven 中央仓库。</p>
<p>关于工具包的 API 说明，已经更新到我的博客网站，欢迎查阅。建议大家多查看 javadoc 文档信息，毕竟我无法将每个 API 函数都演示一下</p>

### Description of the repository (pdc-common-tools)
<p>This is a toolkit that I developed myself using Java 8. </p>
<p>The program has been uploaded to the Maven Central repository.</p>
<p>The API description of the toolkit has been updated to my blog site, please check it out for yourself. I recommend that you check the javadoc documentation as I can't demonstrate every API function.</p>

----------------------------------------------------

### repository config

_maven:_
```xml
<dependency>
	<groupId>io.github.pdcgithub</groupId>
	<artifactId>pdc-common-tool</artifactId>
	<version>1.0.1-jre8-RC</version>
</dependency>
```

_gradle:_
```
implementation group: 'io.github.pdcgithub', name: 'pdc-common-tool', version: '1.0.1-jre8-RC'
```
_gradle(short):_
```
implementation 'io.github.pdcgithub:pdc-common-tool:1.0.1-jre8-RC'
```
_gradle(kotlin):_
```
implementation("io.github.pdcgithub:pdc-common-tool:1.0.1-jre8-RC")
```

### version notes
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