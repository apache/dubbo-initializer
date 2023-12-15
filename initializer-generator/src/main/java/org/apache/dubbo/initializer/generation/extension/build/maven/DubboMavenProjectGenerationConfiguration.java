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
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;

import org.apache.dubbo.initializer.generation.condition.ConditionalOnDubboIdlDependency;
import org.apache.dubbo.initializer.generation.condition.ConditionalOnRequestedModule;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import org.springframework.context.annotation.Bean;

@InitializerProjectGenerationConfiguration
@ConditionalOnBuildSystem(MavenBuildSystem.ID)
public class DubboMavenProjectGenerationConfiguration {

    @Bean
    @ConditionalOnRequestedArchitecture("dubbo")
    @ConditionalOnRequestedModule(value = "api")
    public ApiMavenPluginCustomizer apiMavenPluginCustomizer() {
        return new ApiMavenPluginCustomizer();
    }

    // Root Maven Bom remove in MulitModuleMavenBuildProjectContributor
    @Bean
    @ConditionalOnRequestedArchitecture("dubbo")
    @ConditionalOnRequestedModule(value = "root")
    public RootMavenPluginCustomizer rootMavenPluginCustomizer() {
        return new RootMavenPluginCustomizer();
    }

    @Bean
    @ConditionalOnRequestedArchitecture("dubbo")
    @ConditionalOnRequestedModule({"root"})
    public RootMavenDependenciesCustomizer defaultMavenDependenciesCustomizer() {
        return new RootMavenDependenciesCustomizer();
    }

    @Bean
    @ConditionalOnDubboIdlDependency
    @ConditionalOnRequestedDependency("dubbo-idl")
    public IdlMavenPluginCustomizer IdlMavenPluginCustomizer() {
        return new IdlMavenPluginCustomizer();
    }

    @Bean
    @ConditionalOnDubboIdlDependency
    @ConditionalOnRequestedDependency("dubbo-feature-native")
    public NativeMavenPluginCustomizer nativeMavenPluginCustomizer(ProjectDescription description) {
        return new NativeMavenPluginCustomizer(description);
    }

}
