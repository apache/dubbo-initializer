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

package org.apache.dubbo.initializer.generation.condition;

import com.alibaba.initializer.metadata.Architecture;
import com.alibaba.initializer.metadata.Module;
import com.alibaba.initializer.project.InitializerProjectDescription;
import io.spring.initializr.generator.condition.ProjectGenerationCondition;
import io.spring.initializr.generator.project.ProjectDescription;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

public class OnDubboIdlCondition extends ProjectGenerationCondition {

	@Override
	protected boolean matches(ProjectDescription description, ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		InitializerProjectDescription initializerDescriptiuon = (InitializerProjectDescription)description;
		Architecture architecture = initializerDescriptiuon.getArchitecture();
		if (architecture.getId().equals("none")) {
			return true;
		}

		Module module = context.getBeanFactory().getBean(Module.class);
		String requestModuleName = module.getName() == null && module.isRoot() ? "root" : module.getName();
		return requestModuleName.equals("service");
	}
}
