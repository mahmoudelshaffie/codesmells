package org.codesmells.good;

import java.util.LinkedList;
import java.util.List;

public class HttpErrorResponseHandlersChain implements IHttpErrorResponseHandler {

	private List<IHttpErrorResponseHandler> chain;
	
	public HttpErrorResponseHandlersChain() {
		this.chain = new LinkedList<>();
	}
	
	@Override
	public void handle(HttpResponse response) throws Exception {
		for (IHttpErrorResponseHandler handler: chain) {
			handler.handle(response);
		}
	}	
	
	public void register(IHttpErrorResponseHandler handler) {
		this.chain.add(handler);
	}

}
