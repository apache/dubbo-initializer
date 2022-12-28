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

package org.apache.dubbo.initializer.generation.extension.build.maven.architecture.pic.inter;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * after MavenComplierPluginCustomizer,
 * <pre>
 * 1.Remove maven-compiler-plugin plugin from pom.xml
 * </pre>
 * @author Weix Sun
 * @see com.alibaba.initializer.generation.extension.build.maven.MavenComplierPluginCustomizer
 */
public class InterfaceModuleMavenComplierPluginCustomizer implements BuildCustomizer<MavenBuild> {

    public final int MavenComplierPluginCustomizer_After = 0 + 1;

    private final ProjectDescription description;

    public InterfaceModuleMavenComplierPluginCustomizer(ProjectDescription description) {
        this.description = description;
    }

    @Override
    public void customize(MavenBuild build) {
        build.plugins().remove("org.apache.maven.plugins", "maven-compiler-plugin");
    }

    @Override
    public int getOrder() {
        return MavenComplierPluginCustomizer_After;
    }
}
