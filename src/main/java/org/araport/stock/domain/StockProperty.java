package org.araport.stock.domain;

public class StockProperty {

	private int stockPropertyId;
	private long stockId;
	private int typeId;
	private String value;
	private int rank;
	
	public StockProperty(){
		
	}

	public StockProperty(int stockPropertyId, long stockId, int typeId,
			String value, int rank) {
		super();
		this.stockPropertyId = stockPropertyId;
		this.stockId = stockId;
		this.typeId = typeId;
		this.value = value;
		this.rank = rank;
	}

	public int getStockPropertyId() {
		return stockPropertyId;
	}

	public void setStockPropertyId(int stockPropertyId) {
		this.stockPropertyId = stockPropertyId;
	}

	public long getStockId() {
		return stockId;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return "StockProperty [stockPropertyId=" + stockPropertyId
				+ ", stockId=" + stockId + ", typeId=" + typeId + ", value="
				+ value + ", rank=" + rank + "]";
	}
	
	
	
}
