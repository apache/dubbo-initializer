/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.dubbo.initializer.generation.extension.dependency;

import com.alibaba.initializer.metadata.Module;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * 1.Add protobuf Plugin for the API module   after  ApiMavenPluginCustomizer
 *
 * @author <a href="mailto:15835991162@163.com">ErDan Wang</a>
 */
public class DubboGrpcPluginCustomizer implements BuildCustomizer<MavenBuild> {

    @Autowired
    private Module module;

    @Override
    public void customize(MavenBuild build) {
        build.plugins().add("kr.motd.maven", "os-maven-plugin", builder -> {
            builder.execution("os-maven-plugin", executionBuilder -> {
                executionBuilder.phase("initialize");
                executionBuilder.goal("detect");
            });
            builder.version("1.7.1");
        });
        build.plugins().add("org.xolstice.maven.plugins", "protobuf-maven-plugin",
                builder -> {
                    builder.configuration(configuration -> {
                                configuration.add("protocArtifact", "com.google.protobuf:protoc:3.7.1:exe:${os.detected.classifier}");
                                configuration.add("pluginId", "grpc-java");
                                configuration.add("pluginArtifact", "io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}");
                                configuration.add("protocPlugins", nestedConfiguration -> nestedConfiguration.add("protocPlugin", nestedConfiguration1 -> {
                                    nestedConfiguration1.add("id", "dubbo-grpc");
                                    nestedConfiguration1.add("groupId", "org.apache.dubbo");
                                    nestedConfiguration1.add("artifactId", "dubbo-compiler");
                                    nestedConfiguration1.add("version", "0.0.1");
                                    nestedConfiguration1.add("mainClass", "org.apache.dubbo.gen.grpc.DubboGrpcGenerator");
                                }));
                            }

                    );
                    builder.execution("protobuf-maven-plugin", executionBuilder -> {
                        executionBuilder.goal("compile");
                        executionBuilder.goal("compile-custom");
                    });
                    builder.version("0.6.1");
                }
        );
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
