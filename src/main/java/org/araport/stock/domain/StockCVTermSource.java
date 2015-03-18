package org.araport.stock.domain;

public class StockCVTermSource {
	
	private long stockId;
	private String cvTermId;
	private String pubId;
	private boolean is_not;
	private int rank;
	

	public long getStockId() {
		return stockId;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
	}

	public String getCvTermId() {
		return cvTermId;
	}

	public void setCvTermId(String cvTermId) {
		this.cvTermId = cvTermId;
	}

	public String getPubId() {
		return pubId;
	}

	public void setPubId(String pubId) {
		this.pubId = pubId;
	}

	public boolean isIs_not() {
		return is_not;
	}

	public void setIs_not(boolean is_not) {
		this.is_not = is_not;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public StockCVTermSource(){
		
	}
	
	public StockCVTermSource(long stockId, String cvTermId, boolean is_not) {
		super();
		this.stockId = stockId;
		this.cvTermId = cvTermId;
		this.is_not = is_not;
	}
	
	@Override
	public String toString() {
		return "StockCVTermSource [stockId=" + stockId + ", cvTermId="
				+ cvTermId + ", pubId=" + pubId + ", is_not=" + is_not
				+ ", rank=" + rank + "]";
	}

}
