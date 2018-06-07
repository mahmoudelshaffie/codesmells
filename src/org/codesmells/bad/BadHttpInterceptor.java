package org.codesmells.bad;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * To intercept and serve as general handler for HTTP requests
 * among microservices. The client microservice, which request 
 * over HTTP an information from another one, use 
 * @author elshaffie
 *
 */
public class BadHttpInterceptor implements HttpInterceptor {
	
	private static final int MAX_RETRIES = 10;
	private static final int INITIAL_BACKOFF = 5;
	private ExecutorService executorService;
	private volatile int reloginTrials = 0;
	private volatile int BACKOFF;
	
	public BadHttpInterceptor() {
		this.executorService = Executors.newSingleThreadExecutor();
		this.BACKOFF = INITIAL_BACKOFF;
	}
	
	public void retryLogin(HttpRepsonse response) {
        this.logger.log("Re-login request failed");
        if (response.getStatus() == AuthService.STATUS_SESSION_EXPIRED) {
            this.authService.logout();
            throw new SessionExpiredException();
        } else if (this.reloginTrials >= this.MAX_RETRIES) {
            this.authService.logout();
        } else {
        	if (this.reloginTrials > this.MAX_RETRIES / 2) {
        		this.BACKOFF += INITIAL_BACKOFF;
        	}
            this.logger.log("Retrying re-login");
            // Increment Trials
            ++ this.reloginTrials;

            Future<Boolean> future = this.executorService.submit(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return this.authService.tryRefresh();
				}
            	
            });
            Boolean refreshed = future.get(10, TimeUnit.MILLISECONDS);
            if (refreshed) {
            	this.finalizeReLogin();
            } else {
            	this.retryOrFail();
            }
        }
    }
	
	public void finalizeReLogin() {
        this.reloginTrials = 0;
        this.BACKOFF = INITIAL_BACKOFF;
        if (!this.executorService.isTerminated() 
        		&& !this.executorService.isShutdown()) {
        	this.executorService.shutdownNow();
        }
    }

	/**
	 * Called each time a HTTP Request Is Sent
	 * @param request
	 */
	@Override
	public void onRequest(HttpRequest request) {
		Token token = this.authService.getToken();
		if (token != null) {
			if (token instance of JWTToken) {
				request.setHeader("Authorization", "Bearer " + token.toString());
			} else {				
				request.setHeader("Authorization", token.toString());
			}
		}
	}
	
	@Override
	public void onResponseError(HttpResponse response) {
        if (response.getStatus() == 404) {
            if (response.getConfig().is404Ignored() == true) {
                // Log as error response only
            } else {
                throw new HttpResourceNotFoundException(exData); // Throw an error
            }
        } else if (response.getStatus() == 401) {
            if (! authService.checkIfTokenHasExpired()) {
                // The token is still valid but we received a 401. unexpected error
                throw new UnExpectedInternalError();
            }

            this.logger.log("Performing re-login request");
            
            this.logger.debug("Wait for", this.BACKOFF, "milliseconds and then login with existing session.");
            
            Future<Boolean> future = this.executorService.submit(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return this.authService.tryRefresh();
				}
            	
            });
            Boolean refreshed = future.get(10, TimeUnit.MILLISECONDS);
            if (refreshed) {
            	this.finalizeReLogin();
            } else {
            	this.retryOrFail();
            }
        } else {// Statuscode not {401, 403, 404}
            if (response.getStatus() == 500) {
                // Internal server error: log the exception and display standard error
                throw new InternalHttpError();
            } else if (response.getStatus() == 503) {
                throw new UnavailableHttpError();
            } else {
                // Standard 400 error 
            	throw new StandardHttpError(response.getStatus());
            }
        }
	}
}
