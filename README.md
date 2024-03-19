# JavaRunScript
A Java project that runs script files in other languages on the backend
在后端运行其他语言脚本文件的`Java`项目

## 整体说明
原本是一个用以调度后台`Python`脚本的文件，现在将敏感信息脱离，抽出为单体项目

### 技术栈：
- Java工具包
- Spring-Boot
- Mysql
- Mybatis-plus
- Sa-Token
- knife4j
- 阿里云OSS Java SDK
- Hutool

## 使用说明

仅就单体裸机运行环境，之后有空的话会更新`docker`镜像打包文件

### 全局配置
在`resource`目录下的`application.properties`和`application.yml`自定义全局配置，设置脚本目录、`OSS`的`accessKey`等

### 依赖导入
将`pom.xml`中依赖借助`Maven`导入

### 脚本设置
脚本文件应该放在`wordDir/`下的`scripts/`目录中

### 数据库导入
在`resource`目录下有个`data.sql`脚本，可以一键运行脚本导入架构

## 功能自定义

如果需要运行其他语言类型的脚本，主要更改`util`包下`FileUtil`工具类和`Service`下`ScriptAnalysisService`和其实现类

如果需要可联系本人或者在本仓库提交`Issue`
`mail to me`:
- `Gmail`： [yuyangcoding@gmail.com](yuyangcoding@gmail.com)
- `QQEmail`: [lxksy711@qq.com](lxksy711@qq.com)
