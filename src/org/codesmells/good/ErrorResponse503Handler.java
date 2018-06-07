package org.codesmells.good;

import org.codesmells.bad.HttpResourceNotFoundException;
import org.codesmells.bad.UnavailableHttpError;

public class ErrorResponse503Handler implements IHttpErrorResponseHandler {

	@Override
	public void handle(HttpResponse response) throws Exception {
		throw new UnavailableHttpError();
	}

}
