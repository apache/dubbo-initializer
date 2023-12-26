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
import io.spring.initializr.generator.buildsystem.maven.MavenProfile;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

public class NativeMavenPluginCustomizer implements BuildCustomizer<MavenBuild> {

    private ProjectDescription description;

    public NativeMavenPluginCustomizer(ProjectDescription description) {
        this.description = description;
    }

    @Override
    public void customize(MavenBuild mavenBuild) {
        MavenProfile profile = mavenBuild.profiles().id("native");

        profile.plugins().add("org.springframework.boot", "spring-boot-maven-plugin", builder -> {
            builder.version("${spring-boot.version}");
            builder.configuration(conf -> conf.add("mainClass", description.getPackageName() + "." + description.getApplicationName()));
            builder.execution("process-aot", executionBuilder -> {
                executionBuilder.goal("process-aot");
            });
//            builder.execution("repackage", execution -> execution.goal("repackage"));
        });

        profile.plugins().add("org.graalvm.buildtools", "native-maven-plugin", builder -> {
            builder.version("0.9.20")
                    .execution("add-reachability-metadata", executionBuilder -> {
                        executionBuilder.goal("add-reachability-metadata");
                    })
                    .configuration(configurationBuilder -> {
                        configurationBuilder.add("classDirectory", "${project.build.outputDirectory}");
                        configurationBuilder.add("metadataRepository", repositoryBuilder -> {
                            repositoryBuilder.add("enabled", "true");
                        });
                    })
                    .build();
        });

        profile.plugins().add("org.apache.dubbo", "dubbo-maven-plugin", builder -> {
            builder.version("${dubbo.version}")
                    .execution("dubbo-process-aot", executionBuilder -> {
                        executionBuilder.phase("process-sources").goal("dubbo-process-aot");
                    })
                    .configuration(configurationBuilder -> {
                        configurationBuilder.add("mainClass", "com.example.nativedemo.NativeDemoApplication");
                    })
                    .build();
        });
    }
}
