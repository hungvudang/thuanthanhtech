package com.thuanthanhtech;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hungv
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	public static String ROOT_PATH_PUBLIC_BASE = "/public";
	
	static {
		String osName = System.getProperty("os.name");
		String userHomeDir = System.getProperty("user.name");
		if (osName.toLowerCase().contains("linux")) {
			ROOT_PATH_PUBLIC_BASE = userHomeDir + File.separatorChar + "public";
		}
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);

		Path uploadDirRootBase = Paths.get(ROOT_PATH_PUBLIC_BASE);
		String fRootBase = uploadDirRootBase.toFile().getAbsolutePath();
		registry.addResourceHandler("/" + ROOT_PATH_PUBLIC_BASE + "/**")
				.addResourceLocations("file:/" + fRootBase + "/");
	}
}
