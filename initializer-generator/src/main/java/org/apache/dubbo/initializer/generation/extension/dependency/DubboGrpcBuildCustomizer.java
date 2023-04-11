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

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import java.util.HashSet;
import java.util.Set;

/**
 * 1.Add grpc-netty dependency exclude
 *
 * @author <a href="mailto:15835991162@163.com">ErDan Wang</a>
 */
public class DubboGrpcBuildCustomizer implements BuildCustomizer<MavenBuild> {


    @Override
    public void customize(MavenBuild build) {
        //1.Add grpc-netty dependency exclude
        Set<Dependency.Exclusion> exclusions = new HashSet<>();
        exclusions.add(new Dependency.Exclusion("io.netty", "netty-codec-http2"));
        exclusions.add(new Dependency.Exclusion("io.netty", "netty-handler-proxy"));
        build.dependencies().add("grpc-netty", Dependency.withCoordinates("io.grpc", "grpc-netty").exclusions(exclusions));
    }

}
