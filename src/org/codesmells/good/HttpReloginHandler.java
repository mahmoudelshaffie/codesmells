package org.codesmells.good;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.codesmells.bad.HttpRepsonse;
import org.codesmells.bad.SessionExpiredException;

public class HttpReloginHandler {
	
	public static HttpReloginHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HttpReloginHandler();
		}
		
		return INSTANCE;
	}
	
	private static HttpReloginHandler INSTANCE;
	private static final int MAX_RETRIES = 10;
	private static final int INITIAL_BACKOFF = 5;
	
	private ExecutorService executorService;
	private volatile int reloginTrials = 0;
	private volatile int BACKOFF;

	private HttpReloginHandler() {
		this.executorService = Executors.newSingleThreadExecutor();
		this.BACKOFF = INITIAL_BACKOFF;
	}
	
	private void retryLogin(HttpRepsonse response) {
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
	
	private void finalizeReLogin() {
        this.reloginTrials = 0;
        this.BACKOFF = INITIAL_BACKOFF;
        if (!this.executorService.isTerminated() 
        		&& !this.executorService.isShutdown()) {
        	this.executorService.shutdownNow();
        }
    }
	
	public Future<Boolean> relogin() {
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
}
