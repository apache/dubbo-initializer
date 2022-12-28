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

package org.apache.dubbo.initializer.generation.extension.build.maven.architecture.pic.root;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * after SpringBootBomMavenCustomizer,
 * <pre>
 * 1.Remove spring-boot-maven-plugin plugin from root pom.xml
 * </pre>
 *
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 * @see com.alibaba.initializer.generation.extension.build.maven.SpringBootBomMavenCustomizer
 */
public class RootMavenComplierPluginCustomizer implements BuildCustomizer<MavenBuild> {

    public final int SpringBootBomMavenCustomizer_After = 1 + 1;

    private final ProjectDescription description;

    private final InitializrMetadata metadata;

    public RootMavenComplierPluginCustomizer(ProjectDescription description, InitializrMetadata metadata) {
        this.description = description;
        this.metadata = metadata;
    }

    @Override
    public void customize(MavenBuild build) {
        build.plugins().remove("org.springframework.boot", "spring-boot-maven-plugin");
    }

    @Override
    public int getOrder() {
        return SpringBootBomMavenCustomizer_After;
    }
}
