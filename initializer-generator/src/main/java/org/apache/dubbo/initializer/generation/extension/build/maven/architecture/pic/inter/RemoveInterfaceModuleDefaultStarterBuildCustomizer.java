package org.apache.dubbo.initializer.generation.extension.build.maven.architecture.pic.inter;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import org.springframework.core.Ordered;

/**
 * Remove spring-boot-starter dependency from interface module pom.xml
 *
 * @author Weix Sun
 * @see io.spring.initializr.generator.spring.build.DefaultStarterBuildCustomizer
 */
public class RemoveInterfaceModuleDefaultStarterBuildCustomizer implements BuildCustomizer<MavenBuild> {

	static final String DEFAULT_STARTER = "root_starter";

	@Override
	public void customize(MavenBuild build) {
		if (build.dependencies().has(DEFAULT_STARTER)) {
			build.dependencies().remove(DEFAULT_STARTER);
		}
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
