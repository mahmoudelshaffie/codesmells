package org.codesmells.good;

import org.codesmells.bad.HttpInterceptor;
import org.codesmells.bad.HttpRequest;
import org.codesmells.bad.HttpResponse;

public class GoodHttpInterceptor implements HttpInterceptor {
	
	private IHttpRequestHandler requestHandler;
	private IHttpErrorResponseHandler errorResponseHandler;
	
	public GoodHttpInterceptor(IHttpRequestHandler requestHandler, IHttpErrorResponseHandler errorResponseHandler) {
		this.requestHandler = requestHandler;
		this.errorResponseHandler = errorResponseHandler;
	}

	/**
	 * Called each time a HTTP Request Is Sent
	 * @param request
	 */
	@Override
	public void onRequest(HttpRequest request) {
		
	}

	@Override
	public void onResponseError(HttpResponse response) {
		
	}
}
