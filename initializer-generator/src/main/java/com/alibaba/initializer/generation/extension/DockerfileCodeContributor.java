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

package com.alibaba.initializer.generation.extension;

import com.alibaba.initializer.project.InitializerProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class DockerfileCodeContributor implements ProjectContributor {
    private static final String DOCKERFILE_PATH = "/docker/Dockerfile";
    public static final String PLACEHOLDER = "${JDK_IMAGE}";

    private static final Map<String, String> JDK_IMAGE_VERSIONS = Map.of(
        "1.8", "openjdk:8-jdk-alpine",
        "11", "openjdk:11-jdk-alpine",
        "17", "openjdk:17-jdk-alpine",
        "19", "openjdk:19-jdk-alpine"
    );

    @Autowired
    protected InitializerProjectDescription description;

    @Override
    public void contribute(Path projectRoot) throws IOException {
        URL url = getClass().getResource(DOCKERFILE_PATH);
        if (url == null) {
            throw new IllegalArgumentException("Resource not found on classpath: " + DOCKERFILE_PATH);
        }

        try {
            URI uri = url.toURI();
            // Read the file content
            String dockerFileTemplate = Files.readString(Paths.get(uri));

            // Replace the placeholders
            String dockerFileContent = dockerFileTemplate.replace(PLACEHOLDER, JDK_IMAGE_VERSIONS.get(description.getLanguage().jvmVersion()));

            Path targetFile = Files.createFile(projectRoot.resolve("Dockerfile"));

            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(targetFile))) {
               writer.write(dockerFileContent);
            }
        } catch (Exception e) {
            throw new FileSystemNotFoundException("Resource is not located in the file system: " + e.getMessage());
        }
    }

}
