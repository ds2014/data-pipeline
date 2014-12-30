package org.araport.stock.domain;

public class Stock {

	private long stockId;
	private String name;
	private String uniqueName;
	private String description;
	private StockType stockType;
	private boolean isObsolete;
	private int stockTypeId;
	private int dbxrefId;
	private long tairObjectId;
	private int organismId;
	private StockDbXref stockRef = new StockDbXref();
	
	public Stock() {

	}

	public Stock(long stockId, String name, String description,
			StockType stockType) {

		this.stockId = stockId;
		this.name = name;
		this.description = description;
		this.stockType = stockType;
	}

	public Stock(long stockId, String name, String description,
			StockType stockType, boolean isObsolete) {

		this(stockId, name, description, stockType);
		this.isObsolete = isObsolete;
	}

	public Stock(long stockId, String name, String description,
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

	public long getStockId() {
		return stockId;
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

	
	public long getTairObjectId() {
		return tairObjectId;
	}

	public void setTairObjectId(long tairObjectId) {
		this.tairObjectId = tairObjectId;
	}
	
	public int getOrganismId() {
		return organismId;
	}

	public void setOrganismId(int organismId) {
		this.organismId = organismId;
	}

	@Override
	public String toString() {
		return "Stock [stockId=" + stockId + ", name=" + name
				+ ", description=" + description + ", stockType=" + stockType
				+ ", isObsolete=" + isObsolete + ", stockTypeId=" + stockTypeId
				+ ", dbxrefId=" + dbxrefId + ", tairObjectId=" + tairObjectId
				+ ", organismId=" + organismId + "]";
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
	}

	public StockDbXref getStockRef() {
		return stockRef;
	}

	public void setStockRef(StockDbXref stockRef) {
		this.stockRef = stockRef;
	}

}
