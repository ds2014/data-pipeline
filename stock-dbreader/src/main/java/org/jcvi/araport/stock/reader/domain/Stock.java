package org.jcvi.araport.stock.reader.domain;

public class Stock {
	
	private int stockId;
	private String name;
	private String description;
	private StockType stockType;
	
	public Stock(){
		
	}
	
	public Stock(int stockId, String name, String description, StockType stockType ) {
		
		this.stockId = stockId;
		this.name = name;
		this.description = description;
		this.stockType = stockType;
	}

	public StockType getStockType() {
		return stockType;
	}

	public void setStockType(StockType stockType) {
		this.stockType = stockType;
	}

	public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

}
