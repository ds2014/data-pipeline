package org.jcvi.araport.stock.domain;

public class StockType {
	
	private int stockTypeId;
	private String name;
	
	public StockType(){
		
	}
	
	public int getStockTypeId() {
		return stockTypeId;
	}

	public void setStockTypeId(int stockTypeId) {
		this.stockTypeId = stockTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StockType(int stockTypeId, String name) {
		super();
		this.stockTypeId = stockTypeId;
		this.name = name;
	}
	

	
}
