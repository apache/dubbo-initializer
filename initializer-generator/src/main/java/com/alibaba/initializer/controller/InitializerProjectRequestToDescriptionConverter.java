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

package com.alibaba.initializer.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.initializer.metadata.Architecture;
import com.alibaba.initializer.metadata.InitializerMetadata;
import com.alibaba.initializer.project.InitializerProjectDescription;
import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.packaging.Packaging;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.DefaultMetadataElement;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.Type;
import io.spring.initializr.metadata.support.MetadataBuildItemMapper;
import io.spring.initializr.web.project.DefaultProjectRequestToDescriptionConverter;
import io.spring.initializr.web.project.InvalidProjectRequestException;
import io.spring.initializr.web.project.ProjectRequestToDescriptionConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * copyed from {@link DefaultProjectRequestToDescriptionConverter} and add some
 * customization logic
 *
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Slf4j
public class InitializerProjectRequestToDescriptionConverter
        implements ProjectRequestToDescriptionConverter<ProjectRequest> {

    private void customizeMetadataBasedOnDubboVersion(ProjectRequest request, InitializrMetadata metadata) {
        String dubboVersion = request.getDubboVersion();
        metadata.getConfiguration().getEnv().getBoms().forEach((k, v) -> {
            if (k.contains("dubbo")) {
                if (v.getMappings().get(0) != null) {
                    v.getMappings().get(0).setVersion(dubboVersion);
                }
            }
        });
    }


    @Override
    public ProjectDescription convert(ProjectRequest request, InitializrMetadata metadata) {
        customizeMetadataBasedOnDubboVersion(request, metadata);

        InitializerProjectDescription description = new InitializerProjectDescription();
        doConvert(request, description, metadata);
        doAddition(request, description, metadata);
        return description;
    }

    public void doAddition(ProjectRequest request,
                           InitializerProjectDescription description, InitializrMetadata metadata) {

        if (metadata instanceof InitializerMetadata) {
            InitializerMetadata aMetadata = (InitializerMetadata) metadata;

            Architecture arch = aMetadata.getArchitecture().get(request.getArchitecture());
            description.setArchitecture(arch);
        }
    }

    /**
     * Validate the specified {@link io.spring.initializr.web.project.ProjectRequest request} and initialize the specified
     * {@link ProjectDescription description}. Override any attribute of the description
     * that are managed by this instance.
     *
     * @param request     the request to validate
     * @param description the description to initialize
     * @param metadata    the metadata instance to use to apply defaults if necessary
     */
    private void doConvert(io.spring.initializr.web.project.ProjectRequest request, InitializerProjectDescription description,
                           InitializrMetadata metadata) {
        validate(request, metadata);
        String springBootVersion = getSpringBootVersion(request, metadata);
        String dubboVersion = getDubboVersion(request, metadata);

        List<Dependency> resolvedNonDubboDependencies = getResolvedDependencies(request,
                springBootVersion, metadata, false);
        List<Dependency> resolvedDubboDependencies = getResolvedDependencies(request,
                dubboVersion, metadata, true);
        validateDependencyRange(springBootVersion, resolvedNonDubboDependencies, false);
        validateDependencyRange(dubboVersion, resolvedDubboDependencies, true);

        description.setApplicationName(request.getApplicationName());
        description.setArtifactId(request.getArtifactId());
        description.setBaseDirectory(request.getBaseDir());
        description.setBuildSystem(getBuildSystem(request, metadata));
        description.setDescription(request.getDescription());
        description.setGroupId(request.getGroupId());
        description.setLanguage(
                Language.forId(request.getLanguage(), request.getJavaVersion()));
        description.setName(request.getName());
        description.setPackageName(request.getPackageName());
        description.setPackaging(Packaging.forId(request.getPackaging()));
        description.setPlatformVersion(Version.parse(springBootVersion));
        description.setVersion(request.getVersion());

        List<Dependency> resolvedDependencies = new ArrayList<>();
        resolvedDependencies.addAll(resolvedNonDubboDependencies);
        resolvedDependencies.addAll(resolvedDubboDependencies);
        resolvedDependencies
                .forEach((dependency) -> description.addDependency(dependency.getId(),
                        MetadataBuildItemMapper.toDependency(dependency)));
    }

    private void validate(io.spring.initializr.web.project.ProjectRequest request, InitializrMetadata metadata) {
        validatePlatformVersion(request, metadata);
        validateType(request.getType(), metadata);
        validateLanguage(request.getLanguage(), metadata);
        validatePackaging(request.getPackaging(), metadata);
        validateDependencies(request, metadata);
        validatePackageName(request.getPackageName());
        validateBaseDir(request.getBaseDir());
    }

    private void validateBaseDir(String baseDir) {
        if (StringUtils.isBlank(baseDir)) {
            return;
        }
        if (baseDir.startsWith("/") || baseDir.contains("..")) {
            throw new InvalidProjectRequestException("Invalid baseDir '" + baseDir);
        }
    }

    private void validatePackageName(String packageName) {
        if (StringUtils.isBlank(packageName)) {
            return;
        }
        if (packageName.contains("/")) {
            throw new InvalidProjectRequestException("Invalid packageName '" + packageName);
        }
    }

    private void validatePlatformVersion(io.spring.initializr.web.project.ProjectRequest request, InitializrMetadata metadata) {
        Version platformVersion = Version.safeParse(request.getBootVersion());
        InitializrConfiguration.Platform platform = metadata.getConfiguration().getEnv().getPlatform();
        if (platformVersion != null && !platform.isCompatibleVersion(platformVersion)) {
            throw new InvalidProjectRequestException("Invalid Spring Boot version '" + platformVersion
                    + "', Spring Boot compatibility range is " + platform.determineCompatibilityRangeRequirement());
        }
    }

    private void validateType(String type, InitializrMetadata metadata) {
        if (type != null) {
            Type typeFromMetadata = metadata.getTypes().get(type);
            if (typeFromMetadata == null) {
                throw new InvalidProjectRequestException(
                        "Unknown type '" + type + "' check project metadata");
            }
            if (!typeFromMetadata.getTags().containsKey("build")) {
                throw new InvalidProjectRequestException("Invalid type '" + type
                        + "' (missing build tag) check project metadata");
            }
        }
    }

    private void validateLanguage(String language, InitializrMetadata metadata) {
        if (language != null) {
            DefaultMetadataElement languageFromMetadata = metadata.getLanguages()
                    .get(language);
            if (languageFromMetadata == null) {
                throw new InvalidProjectRequestException(
                        "Unknown language '" + language + "' check project metadata");
            }
        }
    }

    private void validatePackaging(String packaging, InitializrMetadata metadata) {
        if (packaging != null) {
            DefaultMetadataElement packagingFromMetadata = metadata.getPackagings()
                    .get(packaging);
            if (packagingFromMetadata == null) {
                throw new InvalidProjectRequestException(
                        "Unknown packaging '" + packaging + "' check project metadata");
            }
        }
    }

    private void validateDependencies(io.spring.initializr.web.project.ProjectRequest request, InitializrMetadata metadata) {
        List<String> dependencies = request.getDependencies();
        dependencies.forEach((dep) -> {
            Dependency dependency = metadata.getDependencies().get(dep);
            if (dependency == null) {
                throw new InvalidProjectRequestException(
                        "Unknown dependency '" + dep + "' check project metadata");
            }
        });
    }

    private void validateDependencyRange(String version, List<Dependency> resolvedDependencies, boolean isDubbo) {
        resolvedDependencies.forEach((dep) -> {
            if (!dep.match(Version.parse(version))) {
                throw new InvalidProjectRequestException(
                        "Dependency '" + dep.getId() + "' is not compatible "
                                + "with " + (isDubbo ? "Dubbo" : "Spring Boot ") + version);
            }
        });
    }

    private BuildSystem getBuildSystem(io.spring.initializr.web.project.ProjectRequest request, InitializrMetadata metadata) {
        Type typeFromMetadata = metadata.getTypes().get(request.getType());
        return BuildSystem.forId(typeFromMetadata.getTags().get("build"));
    }

    private String getSpringBootVersion(io.spring.initializr.web.project.ProjectRequest request, InitializrMetadata metadata) {
        return (request.getBootVersion() != null) ? request.getBootVersion()
                : metadata.getBootVersions().getDefault().getId();
    }

    private String getDubboVersion(io.spring.initializr.web.project.ProjectRequest request, InitializrMetadata metadata) {
        InitializerMetadata customizedMetadata = (InitializerMetadata)metadata;
        ProjectRequest customizedRequest = (ProjectRequest)request;

        return (customizedRequest.getDubboVersion() != null) ? customizedRequest.getDubboVersion()
                : customizedMetadata.getDubboVersions().getDefault().getId();
    }

    private List<Dependency> getResolvedDependencies(io.spring.initializr.web.project.ProjectRequest request, String springBootVersion, InitializrMetadata metadata, boolean isDubbo) {
        List<String> depIds = getDependenciesWithDefaultComposition(request);
        Version requestedVersion = Version.parse(springBootVersion);
        return depIds.stream()
                .filter(depId -> (isDubbo && depId.contains("dubbo")) || (!isDubbo && !depId.contains("dubbo")))
                .map((it) -> {
                    Dependency dependency = metadata.getDependencies().get(it);
                    return dependency.resolve(requestedVersion);
                }).collect(Collectors.toList());
    }

    private static List<String> getDependenciesWithDefaultComposition(io.spring.initializr.web.project.ProjectRequest request) {
        ProjectRequest projectRequest = (ProjectRequest)request;
        List<String> depIds = request.getDependencies();
        // If user click 'generate' button directly without adding any dependency.
        if (CollectionUtils.isEmpty(depIds)) {
            depIds = new ArrayList<>();
            depIds.add("dubbo");
            if (parseInt(projectRequest.getDubboVersion()) < 30300) {
                depIds.add("dubbo-registry-zookeeper");
            } else {
                depIds.add("dubbo-registry-zookeeper-starter");
            }
            depIds.add("dubbo-protocol-triple");
        } else {
            if (depIds.stream().noneMatch(v -> v.contains("-protocol-"))) {
                depIds.add("dubbo-protocol-triple");
            }
            if (depIds.stream().noneMatch(v -> v.contains("-registry-"))) {
                if (parseInt(projectRequest.getDubboVersion()) < 30300) {
                    depIds.add("dubbo-registry-zookeeper");
                } else {
                    depIds.add("dubbo-registry-zookeeper-starter");
                }
            }
            if (depIds.stream().noneMatch(v -> v.equals("dubbo") || v.equals("dubbo-idl"))) {
                depIds.add("dubbo");
            }
        }
        return depIds;
    }

    public static int getIntVersion(String version) {
        int v;
        try {
            v = parseInt(version);
            // e.g., version number 2.6.3 will convert to 2060300
            if (version.split("\\.").length == 3) {
                v = v * 100;
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Invalid dubbo version: " + version, e);
        }
        return v;
    }

    private static int parseInt(String version) {
        int v = 0;
        String[] vArr = version.split("\\.");
        int len = vArr.length;
        for (int i = 0; i < len; i++) {
            String subV = getPrefixDigits(vArr[i]);
            if (StringUtils.isNotEmpty(subV)) {
                v += Integer.parseInt(subV) * Math.pow(10, (len - i - 1) * 2);
            }
        }
        return v;
    }

    /**
     * get prefix digits from given version string
     */
    private static String getPrefixDigits(String v) {
        Matcher matcher = PREFIX_DIGITS_PATTERN.matcher(v);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private static final Pattern PREFIX_DIGITS_PATTERN = Pattern.compile("^([0-9]*).*");

}
