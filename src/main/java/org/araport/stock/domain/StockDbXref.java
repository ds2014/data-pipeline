package org.araport.stock.domain;

public class StockDbXref {

	private int stockDbXrefId;
	private Long stockId;
	private int dbXrefId;
	private boolean isCurrent;
	
	public StockDbXref(){
		
	}
	
	public int getStockDbXrefId() {
		return stockDbXrefId;
	}

	public void setStockDbXrefId(int stockDbXrefId) {
		this.stockDbXrefId = stockDbXrefId;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public int getDbXrefId() {
		return dbXrefId;
	}

	public void setDbXrefId(int dbXrefId) {
		this.dbXrefId = dbXrefId;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public StockDbXref(int stockDbXrefId, Long stockId, int dbXrefId,
			boolean isCurrent) {
		super();
		this.stockDbXrefId = stockDbXrefId;
		this.stockId = stockId;
		this.dbXrefId = dbXrefId;
		this.isCurrent = isCurrent;
	}
}
