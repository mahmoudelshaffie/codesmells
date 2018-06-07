package org.codesmells.good;

import org.codesmells.bad.StandardHttpError;

public class Standard400ErrorResponseHandler implements IHttpErrorResponseHandler {

	@Override
	public void handle(HttpResponse response) throws Exception {
		throw new StandardHttpError(response.getStatus());
	}
	
}
