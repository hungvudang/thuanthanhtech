package com.thuanthanhtech;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.thuanthanhtech.entities.NewsHelper;
import com.thuanthanhtech.entities.UserHelper;

/**
 * @author hungv
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);

		// Config cho thư mục upload ảnh thumbnail của tin tức
		// ============================================================================
		Path uploadDirForNews = Paths.get(NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM);
		String fUploadPathForNews = uploadDirForNews.toFile().getAbsolutePath();
		registry.addResourceHandler("/" + NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM + "/**")
				.addResourceLocations("file:/" + fUploadPathForNews + "/");
		// =============================================================================\
		
		// Config cho thư mục upload ảnh đại diện của tài khoản người dùng
		// ==========================================================================
		Path uploadDirForUser = Paths.get(UserHelper.ROOT_PATH_AVATAR_MEDIUM);
		String fUploadPathForUser = uploadDirForUser.toFile().getAbsolutePath();
		registry.addResourceHandler("/" + UserHelper.ROOT_PATH_AVATAR_MEDIUM + "/**")
				.addResourceLocations("file:/" + fUploadPathForUser + "/");

		// ===========================================================================
	}
}
