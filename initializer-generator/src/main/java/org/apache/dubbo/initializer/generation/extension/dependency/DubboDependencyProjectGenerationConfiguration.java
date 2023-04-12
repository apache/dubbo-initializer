/*
 * Copyright 2012-2022 the original author or authors.
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

package org.apache.dubbo.initializer.generation.extension.dependency;

import com.alibaba.initializer.generation.condition.ConditionalOnRequestedArchitecture;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import org.apache.dubbo.initializer.generation.condition.ConditionalOnRequestedModule;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="mailto:15835991162@163.com">ErDan Wang</a>
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedArchitecture("dubbo")
class DubboDependencyProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("dubbo-protocol-grpc")
	public DubboGrpcBuildCustomizer dubboGrpcBuildCustomizer() {
		return new DubboGrpcBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("dubbo-protocol-grpc")
	@ConditionalOnRequestedModule("api")
	public DubboGrpcPluginCustomizer dubboGrpcPluginCustomizer() {
		return new DubboGrpcPluginCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("dubbo-protocol-grpc")
	public DubboGrpcDependenciesCustomizer dubboGrpcDependenciesCustomizer() {
		return new DubboGrpcDependenciesCustomizer();
	}

//	@Bean
//	@ConditionalOnRequestedDependency("dubbo-protocol-default")
//	@ConditionalOnRequestedModule("api")
//	public ApiMavenDependenciesCustomizer defaultMavenDependenciesCustomizer() {
//		return new ApiMavenDependenciesCustomizer();
//	}


}
