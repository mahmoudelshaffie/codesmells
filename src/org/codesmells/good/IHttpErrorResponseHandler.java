package org.codesmells.good;

public interface IHttpErrorResponseHandler {
	void handle(HttpResponse response) throws Exception;
}
