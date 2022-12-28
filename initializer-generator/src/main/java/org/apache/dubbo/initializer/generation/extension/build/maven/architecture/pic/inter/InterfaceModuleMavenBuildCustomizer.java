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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.initializer.generation.extension.build.maven.SpringBootBomMavenCustomizer;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenPlugin;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * after SpringBootBomMavenCustomizer,
 * <pre>
 * 1.Remove other all plugins from interface module pom.xml
 * 2.Remove all dependencies from interface module pom.xml
 * </pre>
 *
 * @author Weix Sun
 * @see SpringBootBomMavenCustomizer
 */
public class InterfaceModuleMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

    public final int SpringBootBomMavenCustomizer_After = 1 + 1;

    private final ProjectDescription description;

    private final InitializrMetadata metadata;

    public InterfaceModuleMavenBuildCustomizer(ProjectDescription description, InitializrMetadata metadata) {
        this.description = description;
        this.metadata = metadata;
    }

    @Override
    public void customize(MavenBuild build) {
        // Remove other all plugins
        Map<String, String> pluginCoordinates = build.plugins().values()
                .collect(Collectors.toUnmodifiableMap(MavenPlugin::getGroupId, MavenPlugin::getArtifactId));
        pluginCoordinates.forEach((groupId, artifactId) -> build.plugins().remove(groupId, artifactId));

        // Remove all dependencies
        List<String> ids = build.dependencies().ids().toList();
        ids.forEach(id -> build.dependencies().remove(id));
    }

    @Override
    public int getOrder() {
        return SpringBootBomMavenCustomizer_After;
    }
}
