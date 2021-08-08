package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName = "user-photos";
		Path userPhotosDir = Paths.get(dirName);
		
		String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/"+dirName+"/**")
		         .addResourceLocations("file:/"+userPhotosPath+"/");
		
		String categoryImagesDirName = "../category-image";
		Path categoryImagesDir = Paths.get(categoryImagesDirName);
		
		String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/category-image/**")
		         .addResourceLocations("file:/"+categoryImagesPath+"/");
		
		String brandLogosDirName = "../brand-logos";
		Path brandlogosDir = Paths.get(brandLogosDirName);
		
		String brandLogosPath = brandlogosDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/brand-logos/**")
		         .addResourceLocations("file:/"+brandLogosPath+"/");
	}

}
