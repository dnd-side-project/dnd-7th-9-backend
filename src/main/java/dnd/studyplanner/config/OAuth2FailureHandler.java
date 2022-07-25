package dnd.studyplanner.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component

public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		super.onAuthenticationFailure(request, response, exception);


		String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/auth/after/login")
			.queryParam("success", false)
			.queryParam("error", exception.getLocalizedMessage())
			.build().toUriString();

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

}
