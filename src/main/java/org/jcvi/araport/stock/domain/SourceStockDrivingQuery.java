package org.jcvi.araport.stock.domain;

public class SourceStockDrivingQuery {
	
	private long stockId;

	public SourceStockDrivingQuery(){
		
	}
	
	public SourceStockDrivingQuery(int stockId) {
		super();
		this.stockId = stockId;
	}
	
	
	public long getStockId() {
		return stockId;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
	}


	@Override
	public String toString() {
		return "SourceStockDrivingQuery [stockId=" + stockId + "]";
	}
	
}
