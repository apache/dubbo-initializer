/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.initializer.generation.extension.build.maven;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

public class IdlMavenPluginCustomizer implements BuildCustomizer<MavenBuild> {
    @Override
    public void customize(MavenBuild mavenBuild) {
        mavenBuild.plugins().add("org.xolstice.maven.plugins", "protobuf-maven-plugin", builder -> {
            builder.version("0.6.1")
                    .execution("protoc-compile", executionBuilder -> {
                        executionBuilder.goal("compile").goal("test-compile").goal("compile-custom").goal("test-compile-custom");
                    })
                    .configuration(configurationBuilder -> {
                        configurationBuilder.add("protocArtifact", "com.google.protobuf:protoc:${protoc.version}:exe:${os.detected.classifier}");
                        configurationBuilder.add("grpc-java", "io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}");
                        configurationBuilder.add("protocPlugins", protocPluginsBuilder -> {
                            protocPluginsBuilder.add("protocPlugin", protocPluginBuilder -> {
                                protocPluginBuilder.add("id", "dubbo");
                                protocPluginBuilder.add("groupId", "dubbo-compiler");
                                protocPluginBuilder.add("artifactId", "${dubbo.version}");
                                protocPluginBuilder.add("mainClass", "org.apache.dubbo.gen.tri.Dubbo3TripleGenerator");
                            });
                        });
                    })
                    .build();
        });
    }
}
