package com.thuanthanhtech;

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
	
	private final String ROOT_PATH_PUBLIC_BASE = "/public";
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		Path uploadDirRootBase = Paths.get(ROOT_PATH_PUBLIC_BASE);
		String fRootBase = uploadDirRootBase.toFile().getAbsolutePath();
		registry.addResourceHandler("/" + ROOT_PATH_PUBLIC_BASE + "/**")
		.addResourceLocations("file:/" + fRootBase + "/");

//		// Config cho thư mục upload ảnh thumbnail của tin tức
//		// ============================================================================
//		Path uploadDirForNews = Paths.get(NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM);
//		String fUploadPathForNews = uploadDirForNews.toFile().getAbsolutePath();
//		registry.addResourceHandler("/" + NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM + "/**")
//				.addResourceLocations("file:/" + fUploadPathForNews + "/");
//		// =============================================================================\
//
//		// Config cho thư mục upload ảnh đại diện của tài khoản người dùng
//		// ==========================================================================
//		Path uploadDirForUser = Paths.get(UserHelper.ROOT_PATH_AVATAR_MEDIUM);
//		String fUploadPathForUser = uploadDirForUser.toFile().getAbsolutePath();
//		registry.addResourceHandler("/" + UserHelper.ROOT_PATH_AVATAR_MEDIUM + "/**")
//				.addResourceLocations("file:/" + fUploadPathForUser + "/");
//
//		// ===========================================================================
//
//		// Config cho thư mục upload ảnh của slide
//		// ==========================================================================
//		Path uploadDirForSlide = Paths.get(SlideHelper.ROOT_PATH_IMAGE_MEDIUM);
//		String fUploadPathForSlide = uploadDirForSlide.toFile().getAbsolutePath();
//		registry.addResourceHandler("/" + SlideHelper.ROOT_PATH_IMAGE_MEDIUM + "/**")
//				.addResourceLocations("file:/" + fUploadPathForSlide + "/");
//
//		// ===========================================================================
	}
}
