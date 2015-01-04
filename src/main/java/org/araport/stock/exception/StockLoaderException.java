package org.araport.stock.exception;

public class StockLoaderException extends Exception{

private String errorCode = "Unknown_Exception";
	
	public StockLoaderException() {
		
	}
	
    public StockLoaderException(String message)
    {
       super(message);
    }
    
    public StockLoaderException(String message, String errorCode)
    {
       super(message);
       this.errorCode = errorCode;
    }
    
    public String getErrorCode(){
    	return this.errorCode;
    }
	
}
