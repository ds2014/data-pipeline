package org.araport.stock.domain;

public class StockCVTermSource {
	
	private long stockId;
	private int cvTermId;
	private String pubId;
	private boolean predicate;
	private int rank;
	

	public long getStockId() {
		return stockId;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
	}

	public int getCvTermId() {
		return cvTermId;
	}

	public void setCvTermId(int cvTermId) {
		this.cvTermId = cvTermId;
	}

	public String getPubId() {
		return pubId;
	}

	public void setPubId(String pubId) {
		this.pubId = pubId;
	}

	public boolean getPredicate() {
		return predicate;
	}

	public void setPredicate(boolean predicate) {
		this.predicate = predicate;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public StockCVTermSource(){
		
	}
	
	public StockCVTermSource(long stockId, int cvTermId, boolean predicate) {
		super();
		this.stockId = stockId;
		this.cvTermId = cvTermId;
		this.predicate = predicate;
	}
	
	@Override
	public String toString() {
		return "StockCVTermSource [stockId=" + stockId + ", cvTermId="
				+ cvTermId + ", pubId=" + pubId + ", is_not=" + predicate
				+ ", rank=" + rank + "]";
	}

}
