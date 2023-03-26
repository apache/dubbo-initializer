package org.apache.dubbo.initializer.generation.extension.codes.language;

import com.alibaba.initializer.generation.InitializerProjectGenerationConfiguration;
import com.alibaba.initializer.generation.condition.ConditionalOnModule;
import com.alibaba.initializer.generation.condition.ConditionalOnRequestedArchitecture;
import io.spring.initializr.generator.condition.ConditionalOnLanguage;
import io.spring.initializr.generator.language.java.JavaLanguage;

/**
 * @author <a href="mailto:15835991162@163.com">ErDan Wang</a>
 */
@InitializerProjectGenerationConfiguration
@ConditionalOnRequestedArchitecture("dubbo")
@ConditionalOnModule(main = true)
@ConditionalOnLanguage(JavaLanguage.ID)
public class DubboJavaProjectGenerationConfiguration {
}
