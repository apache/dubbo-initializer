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
package com.alibaba.initializer.generation.extension.build;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenPlugin;
import io.spring.initializr.generator.buildsystem.maven.MavenPluginContainer;
import org.springframework.util.CollectionUtils;

public class MavenBuildUtils {

    public static void build(final MavenBuild build, String mainClass) {
        MavenPlugin springBootMavenPlugin = null;
        for (MavenPlugin mavenPlugin : build.plugins().values().toList()) {
            if (mavenPlugin.getArtifactId().equals("spring-boot-maven-plugin")){
                springBootMavenPlugin = mavenPlugin;
            }
        }

        final MavenPlugin plugin = springBootMavenPlugin;

        build.plugins().remove("org.springframework.boot", "spring-boot-maven-plugin");
        if (plugin == null || CollectionUtils.isEmpty(plugin.getExecutions())) {
            build.plugins().add("org.springframework.boot", "spring-boot-maven-plugin", builder -> {
                builder.version("${spring-boot.version}");//这个要用spring version么？
                builder.execution("repackage", execution -> execution.goal("repackage"));
                builder.configuration(conf -> conf.add("mainClass", mainClass).add("skip", "true"));
            });
        } else {
            build.plugins().add("org.springframework.boot", "spring-boot-maven-plugin", builder -> {
                builder.version("${spring-boot.version}");//这个要用spring version么？
                plugin.getExecutions().forEach(execution -> {
                    builder.execution(execution.getId(), executionBuilder -> execution.getGoals().forEach(executionBuilder::goal));
                });
                builder.configuration(conf -> conf.add("mainClass", mainClass).add("skip", "true"));
            });
        }
    }
}
