package dnd.studyplanner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dnd.studyplanner.jwt.JwtTokenInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
	private final JwtTokenInterceptor jwtTokenInterceptor;

	@Value("${front-client.domain}")
	public String DOMAIN_HOST;

	public void addInterceptors(InterceptorRegistry registry) {
		//Interceptor scope
		registry.addInterceptor(jwtTokenInterceptor)
			.addPathPatterns("/token/test/**")
			.addPathPatterns("/**")
			.excludePathPatterns("/css/**","/images/**","/js/**","/h2-console/**", "/profile", "/favicon.ico")
			.excludePathPatterns("/oauth2/**", "/auth/token/reissue", "/user/exist", "/oauth/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedOrigins("*")
			.allowedMethods("*");
	}
}
