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

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * after MavenComplierPluginCustomizer,SpringBootBomMavenCustomizer
 * <pre>
 * 1.Remove other all plugins from api module pom.xml
 * </pre>
 *
 * @author Weix Sun
 * @see com.alibaba.initializer.generation.extension.build.maven.MavenComplierPluginCustomizer,com.alibaba.initializer.generation.extension.build.maven.SpringBootBomMavenCustomizer
 */
public class ApiMavenPluginCustomizer implements BuildCustomizer<MavenBuild> {


    @Override
    public void customize(MavenBuild build) {
        build.plugins().remove("org.apache.maven.plugins", "maven-compiler-plugin");
        build.plugins().remove("org.springframework.boot", "spring-boot-maven-plugin");
        build.dependencies().ids().toList().forEach(id -> {
            build.dependencies().remove(id);
        });
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
