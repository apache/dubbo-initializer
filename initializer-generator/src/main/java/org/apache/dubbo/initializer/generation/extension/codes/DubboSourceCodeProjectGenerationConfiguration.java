package org.apache.dubbo.initializer.generation.extension.codes;

import com.alibaba.initializer.generation.InitializerProjectGenerationConfiguration;
import com.alibaba.initializer.generation.condition.ConditionalOnModule;
import com.alibaba.initializer.generation.condition.ConditionalOnRequestedArchitecture;
import com.alibaba.initializer.generation.extension.SampleCodeContributor;
import io.spring.initializr.generator.condition.ConditionalOnLanguage;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.spring.code.MainApplicationTypeCustomizer;
import org.springframework.context.annotation.Bean;

@InitializerProjectGenerationConfiguration
@ConditionalOnModule(main = true)
@ConditionalOnLanguage(JavaLanguage.ID)
public class DubboSourceCodeProjectGenerationConfiguration {
    @Bean
//    @ConditionalOnRequestedDependency("dubbo")
    public MainApplicationTypeCustomizer<TypeDeclaration> enableDubboAnnotator() {
        return (typeDeclaration) -> {
            typeDeclaration.annotate(Annotation.name("org.apache.dubbo.config.spring.context.annotation.EnableDubbo"));
        };
    }

//    @Bean
//    public DubboSampleCodeContributor dubboSampleCodeContributor() {
//        return new DubboSampleCodeContributor();
//    }
}
