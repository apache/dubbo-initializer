/*
 * Copyright 2012-2020 the original author or authors.
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

package io.spring.initializr.generator.spring.build.maven;

import com.alibaba.initializer.generation.condition.ConditionalOnModule;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that depend on Maven.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
@ConditionalOnBuildSystem(MavenBuildSystem.ID)
class MavenProjectGenerationConfiguration {

	@Bean
	@ConditionalOnPlatformVersion("[2.0.0.M1,3.1.0-RC1)")
	@ConditionalOnModule(root = true)
	public MavenWrapperContributor maven38WrapperContributor() {
		return new MavenWrapperContributor("3.8");
	}

	@Bean
	@ConditionalOnPlatformVersion("3.1.0-RC1")
	@ConditionalOnModule(root = true)
	public MavenWrapperContributor mavenWrapperContributor() {
		return new MavenWrapperContributor("3");
	}

}
