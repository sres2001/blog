package ru.skillbox.blog.config;

import java.util.TimeZone;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlogConfiguration {

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return new NullBeanPostProcessor();
    }

    private static class NullBeanPostProcessor
            implements BeanFactoryPostProcessor {

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
            // nop
        }
    }
}
