package com.leyikao.onlinelearn.serviceapp.util;

import java.lang.reflect.Method;
import java.util.Properties;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.core.io.support.PropertiesLoaderSupport;

public class ResourceHelper {

	private static Properties properties = new Properties();
	static {
		try {
			String[] postProcessorNames = SpringUtil.getBeanNamesForType(BeanFactoryPostProcessor.class, true, true);

			for (String ppName : postProcessorNames) {
				BeanFactoryPostProcessor beanProcessor = SpringUtil.getBean(ppName, BeanFactoryPostProcessor.class);
				if (beanProcessor instanceof PropertyResourceConfigurer) {
					PropertyResourceConfigurer propertyResourceConfigurer = (PropertyResourceConfigurer) beanProcessor;

					Method mergeProperties = PropertiesLoaderSupport.class.getDeclaredMethod("mergeProperties");
					mergeProperties.setAccessible(true);
					Properties props = (Properties) mergeProperties.invoke(propertyResourceConfigurer);

					Method convertProperties = PropertyResourceConfigurer.class.getDeclaredMethod("convertProperties",Properties.class);
					convertProperties.setAccessible(true);
					convertProperties.invoke(propertyResourceConfigurer, props);

					properties.putAll(props);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}
}
