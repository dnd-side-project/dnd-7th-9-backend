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
		registry.addInterceptor(jwtTokenInterceptor).addPathPatterns("/token/test/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedOrigins(DOMAIN_HOST)
			.allowedMethods(
				HttpMethod.GET.name(),
				HttpMethod.POST.name(),
				HttpMethod.PATCH.name(),
				HttpMethod.DELETE.name()
			);
	}
}
