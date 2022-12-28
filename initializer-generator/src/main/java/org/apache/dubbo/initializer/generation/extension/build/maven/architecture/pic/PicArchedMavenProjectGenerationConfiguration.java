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

package org.apache.dubbo.initializer.generation.extension.build.maven.architecture.pic;

import com.alibaba.initializer.generation.condition.ConditionalOnRequestedArchitecture;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;
import org.apache.dubbo.initializer.generation.condition.ConditionalOnRequestedModule;
import org.apache.dubbo.initializer.generation.extension.build.maven.architecture.pic.inter.InterfaceModuleMavenBuildCustomizer;
import org.apache.dubbo.initializer.generation.extension.build.maven.architecture.pic.inter.InterfaceModuleMavenComplierPluginCustomizer;
import org.apache.dubbo.initializer.generation.extension.build.maven.architecture.pic.inter.RemoveInterfaceModuleDefaultStarterBuildCustomizer;
import org.apache.dubbo.initializer.generation.extension.build.maven.architecture.pic.root.RootMavenComplierPluginCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * @author Weix Sun
 */
@ProjectGenerationConfiguration
@ConditionalOnBuildSystem(MavenBuildSystem.ID)
public class PicArchedMavenProjectGenerationConfiguration {

    @Bean
    @ConditionalOnRequestedArchitecture("pic")
    @ConditionalOnRequestedModule("interface")
    public InterfaceModuleMavenBuildCustomizer interfaceModuleMavenBuildCustomizer(
            ProjectDescription description, InitializrMetadata metadata) {
        return new InterfaceModuleMavenBuildCustomizer(description, metadata);
    }

    @Bean
    @ConditionalOnRequestedArchitecture("pic")
    @ConditionalOnRequestedModule("interface")
    public InterfaceModuleMavenComplierPluginCustomizer interfaceModuleMavenComplierPluginCustomizer(
            ProjectDescription description) {
        return new InterfaceModuleMavenComplierPluginCustomizer(description);
    }

    /**
     * DefaultStarterBuildCustomizer and RemoveInterfaceModuleDefaultStarterBuildCustomizer's order all <b>Ordered.LOWEST_PRECEDENCE</b>.
     * However, DefaultStarterBuildCustomizer has high priority.
     *
     * @see io.spring.initializr.generator.spring.build.DefaultStarterBuildCustomizer
     */
    @Bean
    @ConditionalOnRequestedArchitecture("pic")
    @ConditionalOnRequestedModule("interface")
    public RemoveInterfaceModuleDefaultStarterBuildCustomizer removeInterfaceModuleDefaultStarterBuildCustomizer() {
        return new RemoveInterfaceModuleDefaultStarterBuildCustomizer();
    }

    @Bean
    @ConditionalOnRequestedArchitecture("pic")
    @ConditionalOnRequestedModule("root")
    public RootMavenComplierPluginCustomizer rootMavenComplierPluginCustomizer(
            ProjectDescription description, InitializrMetadata metadata) {
        return new RootMavenComplierPluginCustomizer(description, metadata);
    }

}
