[English](README.md) | [简体中文](README-zh.md)

---
# Dubbo Initializer
Here is the instance hosted by Dubbo community [start.dubbo.apache.org](https://start.dubbo.apache.org/).

You can also customize the code to meet your own requirements and deploy one yourself.

## Code structure
Dubbo Initializer is derived from Cloud Native App Initializer, which includes the following modules:
* initializer-generator, java-based codebase that helps to generate the template project.
* initializer-page, ui pages

## Run from source
Please clone the project locally and make sure you have a Java 17 environment.

### Build project
In the project root directory, execute the following commands:
```shell
mvn compile -P install-yarn  -Dmaven.test.skip # Install `Node` and `Yarn`
mvn prepare-package # Copy the static files to the target of the `initializer-generator` module.
```

> If you are running in a ARM-based system and encounter error running above commands, please add `-Dos.arch=x64` and try again.

### Run project
Enter the `initializer-generator` module and execute the following command to start the application:
```shell
cd initializer-generator
mvn spring-boot:run
```
In the browser, enter `http://127.0.0.1:7001/bootstrap.html` to use the initializer project for project bootstrap.

## License
This project is a project under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).