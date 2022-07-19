package dnd.studyplanner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dnd.studyplanner.jwt.JwtTokenInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
	private final JwtTokenInterceptor jwtTokenInterceptor;

	public void addInterceptors(InterceptorRegistry registry) {
		//Interceptor scope
		registry.addInterceptor(jwtTokenInterceptor).addPathPatterns("/token/test/**");
	}
}
