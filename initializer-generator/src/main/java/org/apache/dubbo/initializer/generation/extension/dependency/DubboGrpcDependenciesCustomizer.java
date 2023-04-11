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

package org.apache.dubbo.initializer.generation.extension.dependency;

import com.alibaba.initializer.metadata.Module;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 1.Apart from GRPC dependencies, remove other dependencies of the API
 * 2.GRPC dependency removal for serivce
 *
 * @author <a href="mailto:15835991162@163.com">ErDan Wang</a>
 * after  DependencyManagementBuildCustomizer
 * @see io.spring.initializr.generator.spring.build.DependencyManagementBuildCustomizer
 */
public class DubboGrpcDependenciesCustomizer implements BuildCustomizer<MavenBuild> {


    @Autowired
    private Module module;

    @Override
    public void customize(MavenBuild build) {
        // 1.Apart from GRPC dependencies, remove other dependencies of the API
        // 2.GRPC dependency removal for serivce
        build.dependencies().ids().toList().forEach(id -> {
            boolean has = (id.equalsIgnoreCase("grpc-netty-shaded") || id.equalsIgnoreCase("grpc-protobuf") || id.equalsIgnoreCase("grpc-stub") || id.equalsIgnoreCase("grpc-netty") || id.equalsIgnoreCase("dubbo-common"));
            if ((module.isMain() && has) || (!module.isMain() && !has))
                build.dependencies().remove(id);
        });
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
