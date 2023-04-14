[English](README.md) | [简体中文](README-zh.md)

---
# Dubbo脚手架

这里是 Dubbo 官方社区提供的一个托管实例 [start.dubo.apache.org](https://start.dubbo.apache.org/).

您还可以自定义代码并自行部署以满足定制化需求。

## 代码结构
本项目是在 Cloud Native App Initializer 开源项目基础上改造而来，以下是项目本身包含以下模块：
* initializer-generator: Java 语言开发的脚手架生成组件
* initializer-page: 脚手架前端 UI 页面

## 基于源代码运行
请在本地 clone 该项目，并确保具备 Java 17 环境。

### 构建项目
在项目根目录，执行以下命令：
```shell
mvn compile -P install-yarn  -Dmaven.test.skip # 安装 Node 和 Yarn
mvn prepare-package # 将静态文件 Copy 到 `initializer-generator` 模块的 target 中
```

> 对于 ARM 架构，可以尝试在命令后添加如下参数后重试 `-Dos.arch=x64`

### 启动项目
进入`initializer-generator` 模块，执行以下命令启动应用：


```shell
cd initializer-generator
mvn spring-boot:run
```
在浏览器中，输入 http://127.0.0.1:7001/bootstrap.html 即可使用脚手架项目进行工程构建。

## 项目License
该项目是一个采用 [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html) 的项目。