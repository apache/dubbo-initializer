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

package org.apache.dubbo.initializer.generation.extension.dependency.dubbregisterzookeeper.architecture.pic.consumer;

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
public class ConsumerDubboRegisterZookeeperBuildCustomizer implements BuildCustomizer<Build>, Ordered {

	private static final int DependencyManagementBuildCustomizer_After = Ordered.LOWEST_PRECEDENCE - 5 + 5;

	@Override
	public void customize(Build build) {
		build.dependencies().remove("metrics-core");
	}

	@Override
	public int getOrder() {
		return DependencyManagementBuildCustomizer_After;
	}

}
