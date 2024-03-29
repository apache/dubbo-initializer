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

package com.alibaba.initializer.metadata;

import java.util.Map;

import io.spring.initializr.metadata.DefaultMetadataElement;
import io.spring.initializr.metadata.Defaultable;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.SingleSelectCapability;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
public class InitializerMetadata extends InitializrMetadata {

    private final ArchitectureCapability architecture = new ArchitectureCapability(
            "architecture",
            "application architecture",
            "the architecture of application");

    private final SingleSelectCapability dubboVersions = new SingleSelectCapability(
            "dubboVersion",
            "Apache Dubbo Version",
            "apache dubbo version");


    protected InitializerMetadata(InitializerProperties configuration) {
        super(configuration);
    }

    public Map<String, Object> defaults() {
        Map<String, Object> defaults = super.defaults();
        defaults.put("architecture", defaultId(this.architecture));
        defaults.put("dubboVersion", defaultId(this.dubboVersions));
        return defaults;
    }

    public void merge(InitializrMetadata other) {
        super.merge(other);
        InitializerMetadata initializerMetadata = (InitializerMetadata) other;
        this.architecture.merge(initializerMetadata.architecture);
        this.dubboVersions.merge(initializerMetadata.dubboVersions);
    }

    private static String defaultId(Defaultable<? extends DefaultMetadataElement> element) {
        DefaultMetadataElement defaultValue = element.getDefault();
        return (defaultValue != null) ? defaultValue.getId() : null;
    }

    public ArchitectureCapability getArchitecture() {
        return this.architecture;
    }

    public SingleSelectCapability getDubboVersions() {
        return dubboVersions;
    }

    public InitializerProperties getInitializerConfiguration() {
        return (InitializerProperties) getConfiguration();
    }
}
