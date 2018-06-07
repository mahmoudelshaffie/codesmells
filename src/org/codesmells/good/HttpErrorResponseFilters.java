package org.codesmells.good;

import java.util.HashMap;
import java.util.Map;

public class HttpErrorResponseFilters implements IHttpErrorResponseHandler {

	private Map<Integer, HttpErrorResponseHandlersChain> statusHandlers;
	private IHttpErrorResponseHandler standardHandler;
	
	public HttpErrorResponseFilters() {
		this.statusHandlers = new HashMap<>();
		this.standardHandler = new Standard400ErrorResponseHandler();
	}
	
	@Override
	public void handle(HttpResponse response) throws Exception {
		if(this.statusHandlers.contains(response.getStatus())) {
			this.statusHandlers.get(response.getStatus).handle(response);
		} else {
			this.standardHandler.handle(response);
		}
	}
	
	public void registerHandler(Integer statusCode, IHttpErrorResponseHandler handler) {
		HttpErrorResponseHandlersChain statusHandler = this.statusHandlers.get(statusCode);
		if (statusHandler == null) {
			statusHandler = new HttpErrorResponseHandlersChain();
			this.statusHandlers.put(statusCode, statusHandler);
		}
		statusHandler.register(handler);
	}

}
