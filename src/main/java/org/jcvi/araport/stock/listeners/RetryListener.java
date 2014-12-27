package org.jcvi.araport.stock.listeners;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;

public class RetryListener implements org.springframework.retry.RetryListener{

	@Override
	public <T, E extends Throwable> void close(RetryContext arg0,
			RetryCallback<T, E> arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T, E extends Throwable> void onError(RetryContext arg0,
			RetryCallback<T, E> arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T, E extends Throwable> boolean open(RetryContext arg0,
			RetryCallback<T, E> arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
