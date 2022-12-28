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

package org.apache.dubbo.initializer.generation.extension.dependency.dubbregisterzookeeper;

import java.util.List;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionReference;

import org.springframework.core.Ordered;

/**
 * after DependencyManagementBuildCustomizer
 * @author Weix Sun
 *
 * @see io.spring.initializr.generator.spring.build.DependencyManagementBuildCustomizer
 */
class DubboRegisterZookeeperBuildCustomizer implements BuildCustomizer<Build>, Ordered {

	private static final int DependencyManagementBuildCustomizer_After = Ordered.LOWEST_PRECEDENCE - 5 + 5;

	@Override
	public void customize(Build build) {
		build.properties().version("slf4j-log4j12.version", "1.7.25");

		// Remove all dependencies, after the 'dependencies.bom' analysis is completed
		List<String> ids = build.dependencies().ids().toList();
		ids.forEach(id -> build.dependencies().remove(id));

		// Add additional dependencies
		build.dependencies().add("org.slf4j_slf4j-api", "org.slf4j", "slf4j-api", DependencyScope.COMPILE);

		Dependency slf4jlog4j12Dependency =
				Dependency.withCoordinates("org.slf4j", "slf4j-log4j12")
						.version(VersionReference.ofProperty("slf4j-log4j12.version")).build();
		build.dependencies().add("org.slf4j_slf4j-log4j12", slf4jlog4j12Dependency);

		build.dependencies().add("log4j_log4j", "log4j", "log4j", DependencyScope.COMPILE);

//		// Remove redundant dependencies, after the 'dependencies.bom' analysis is completed
//		build.dependencies().remove("dubbo-register-zookeeper");
//		build.dependencies().remove("metrics-core");
//		build.dependencies().remove("test");




	}

	@Override
	public int getOrder() {
		return DependencyManagementBuildCustomizer_After;
	}

}
