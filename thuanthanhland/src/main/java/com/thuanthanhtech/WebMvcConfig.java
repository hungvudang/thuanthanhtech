package com.thuanthanhtech;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.thuanthanhtech.entities.NewsHelper;

/**
 * @author hungv
 * File config cho việc hiển thị ảnh thumbnail của tin tức
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);

		Path uploadDir = Paths.get(NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM);
		String fUploadPath = uploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/" + NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM + "/**")
				.addResourceLocations("file:/" + fUploadPath + "/");
	}
}
