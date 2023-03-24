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
 * after DefaultStarterBuildCustomizer,
 * <pre>
 * 2.Remove starter dependencies from  pom.xml
 * </pre>
 *
 * @author Weix Sun
 * @see io.spring.initializr.generator.spring.build.DefaultStarterBuildCustomizer
 */
public class DefaultMavenBomCustomizer implements BuildCustomizer<MavenBuild> {

    static final String DEFAULT_STARTER = "root_starter";
    static final String TEST_STARTER = "test";

    @Override
    public void customize(MavenBuild build) {
        if (build.dependencies().has(DEFAULT_STARTER)) {
            build.dependencies().remove(DEFAULT_STARTER);
        }
        if (build.dependencies().has(TEST_STARTER)) {
            build.dependencies().remove(TEST_STARTER);
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
