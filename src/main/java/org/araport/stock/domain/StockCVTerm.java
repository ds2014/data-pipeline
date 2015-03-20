package org.araport.stock.domain;

public class StockCVTerm {
	
	private long stockId;
	private int typeId;
	private int rank;
	private boolean predicate;
	
	public StockCVTerm(){
		
	}

	public long getStockId() {
		return stockId;
	}
	
	public boolean getPredicate(){
		return this.predicate;
	}
	
	public void setPredicate(boolean predicate){
		this.predicate = predicate;
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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public StockCVTerm(long stockId, int typeId) {
		super();
		this.stockId = stockId;
		this.typeId = typeId;
		this.rank = 0;
	}
	
	public StockCVTerm(long stockId, int typeId, int rank) {
		super();
		this.stockId = stockId;
		this.typeId = typeId;
		this.rank = rank;
	}
	
	@Override
	public String toString() {
		return "StockCVTermSource [stockId=" + stockId + ", cvTermId="
				+ typeId + ", rank=" + rank + "]";
	}

}
