package org.codesmells.good;

import org.codesmells.bad.InternalHttpError;

public class ErrorResponse505Handler implements IHttpErrorResponseHandler {

	@Override
	public void handle(HttpResponse response) throws Exception {
		throw new InternalHttpError();
	}

}
