package org.jcvi.araport.stock.domain;

public class Stock {

	private int stockId;
	private String name;
	private String description;
	private StockType stockType;
	private boolean isObsolete;
	private int stockTypeId;
	private int dbxrefId;
	private int tairObjectId;
	
	public Stock() {

	}

	public Stock(int stockId, String name, String description,
			StockType stockType) {

		this.stockId = stockId;
		this.name = name;
		this.description = description;
		this.stockType = stockType;
	}

	public Stock(int stockId, String name, String description,
			StockType stockType, boolean isObsolete) {

		this(stockId, name, description, stockType);
		this.isObsolete = isObsolete;
	}

	public Stock(int stockId, String name, String description,
			int stockTypeId, int dbxrefId, boolean isObsolete) {

		this.stockId = stockId;
		this.name = name;
		this.description = description;
		this.stockTypeId = stockTypeId;
		this.dbxrefId = dbxrefId;
		this.isObsolete = isObsolete;
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

	public boolean isObsolete() {
		return isObsolete;
	}

	public void setObsolete(boolean isObsolete) {
		this.isObsolete = isObsolete;
	}
	
	public int getStockTypeId() {
		return stockTypeId;
	}

	public void setStockTypeId(int stockTypeId) {
		this.stockTypeId = stockTypeId;
	}

	public int getDbxrefId() {
		return dbxrefId;
	}

	public void setDbxrefId(int dbxrefId) {
		this.dbxrefId = dbxrefId;
	}

	
	public int getTairObjectId() {
		return tairObjectId;
	}

	public void setTairObjectId(int tairObjectId) {
		this.tairObjectId = tairObjectId;
	}
}
