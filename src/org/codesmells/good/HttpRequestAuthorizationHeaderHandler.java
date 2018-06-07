package org.codesmells.good;

import org.codesmells.bad.JWTToken;
import org.codesmells.bad.Token;

public class HttpRequestAuthorizationHeaderHandler implements IHttpRequestHandler {
	
	private AuthService authService;
	
	public HttpRequestAuthorizationHeaderHandler(AuthService authService) {
		this.authService = authService;
	}

	@Override
	public void handle(HttpRequest request) throws Exception {
		Token token = this.authService.getToken();
		if (token != null) {
			if (token instance of JWTToken) {
				request.setHeader("Authorization", "Bearer " + token.toString());
			} else {				
				request.setHeader("Authorization", token.toString());
			}
		}
	}

}
