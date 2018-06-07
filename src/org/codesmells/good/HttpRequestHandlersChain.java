package org.codesmells.good;

import java.util.LinkedList;
import java.util.List;

/**
 * Serve as chain of handlers for HttpRequest
 * @author elshaffie
 *
 */
public class HttpRequestHandlersChain implements IHttpRequestHandler {

	private List<IHttpRequestHandler> chain;
	
	public HttpRequestHandlersChain() {
		this.chain = new LinkedList<>();
	}

	@Override
	public void handle(HttpRequest request) throws Exception {
		for (IHttpRequestHandler handler: chain) {
			handler.handle(request);
		}
	}
	
	public void registerHandler(IHttpRequestHandler handler) {
		this.registerHandler(handler);
	}
}
