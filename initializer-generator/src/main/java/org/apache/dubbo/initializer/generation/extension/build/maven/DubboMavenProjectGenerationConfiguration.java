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

package org.apache.dubbo.initializer.generation.extension.build.maven;

import com.alibaba.initializer.generation.InitializerProjectGenerationConfiguration;
import com.alibaba.initializer.generation.condition.ConditionalOnRequestedArchitecture;
import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import org.apache.dubbo.initializer.generation.condition.ConditionalOnRequestedModule;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="mailto:15835991162@163.com">ErDan Wang</a>
 */
@InitializerProjectGenerationConfiguration
@ConditionalOnRequestedArchitecture("dubbo")
@ConditionalOnBuildSystem(MavenBuildSystem.ID)
public class DubboMavenProjectGenerationConfiguration {

    @Bean
    @ConditionalOnRequestedModule(value = "api")
    public ApiMavenPluginCustomizer apiMavenPluginCustomizer() {
        return new ApiMavenPluginCustomizer();
    }

    // Root Maven Bom remove in MulitModuleMavenBuildProjectContributor
    @Bean
    @ConditionalOnRequestedModule(value = "root")
    public RootMavenPluginCustomizer rootMavenPluginCustomizer() {
        return new RootMavenPluginCustomizer();
    }

    @Bean
    @ConditionalOnRequestedModule("root")
    public RootMavenDependenciesCustomizer rootMavenDependenciesCustomizer() {
        return new RootMavenDependenciesCustomizer();
    }


    @Bean
    @ConditionalOnRequestedModule("service")
    public BuildCustomizer<Build> defaultStarterBuildCustomizer() {
        return build -> build.dependencies().add("dubbo-starter", Dependency.withCoordinates("org.apache.dubbo", "dubbo-spring-boot-starter").build());
    }

}
