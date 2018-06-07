package org.codesmells.good;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.codesmells.bad.UnExpectedInternalError;

public class ErrorResponse401Handler implements IHttpErrorResponseHandler {

	@Override
	public void handle(HttpResponse response) throws Exception {
		if (! authService.checkIfTokenHasExpired()) {
            // The token is still valid but we received a 401. unexpected error
            throw new UnExpectedInternalError();
        }

        this.logger.log("Performing re-login request");
        this.logger.debug("Wait for", this.BACKOFF, "milliseconds and then login with existing session.");
        
        HttpReloginHandler.getInstance().relogin().get();   
        
	}
}
