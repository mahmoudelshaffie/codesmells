package org.codesmells.good;

import org.codesmells.bad.HttpResourceNotFoundException;

public class ErrorResponse404Handler implements IHttpErrorResponseHandler {

	@Override
	public void handle(HttpResponse response) throws Exception {
		if (response.getConfig().is404Ignored() == true) {
            // Log as error response only
        } else {
            throw new HttpResourceNotFoundException(exData); // Throw an error
        }
	}

}
