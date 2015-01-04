package org.araport.stock.exception;

import org.apache.commons.lang3.StringUtils;

public class ExceptionLogger {

	private Exception exception;
	private Object item = null;
	
	
	public ExceptionLogger(Exception exception){
		this.exception = exception;
	}
	
	public ExceptionLogger(Exception exception, Object item){
		this.exception = exception;
		this.item = item;
	}
	
	public String getErrorMessage(){
		
		String errorMessage = "";
		
		if (item!=null){
			errorMessage = "Item :" + item.toString();
		}
		
		if (!StringUtils.isBlank(errorMessage)){
		errorMessage = errorMessage + "; Message: " + this.exception.getMessage()
				 + "; Cause: " + this.exception.getCause();
		}else
		{
			errorMessage = " Message: " + this.exception.getMessage()
					 + "; Cause: " + this.exception.getCause();
		}
		
		return errorMessage;
	}
	

	
}
