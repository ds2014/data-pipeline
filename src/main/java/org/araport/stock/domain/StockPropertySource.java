package org.araport.stock.domain;

public class StockPropertySource {
	
	private long stockId;
	private String key;
	private String value;
	private int typeId;
	
	public StockPropertySource(){
		
	}
   
	public StockPropertySource(long stock_id, String key, String value,
			int type_id) {
		super();
		this.stockId = stock_id;
		this.key = key;
		this.value = value;
		this.typeId = type_id;
	}
	
	
	public long getStockId() {
		return stockId;
	}



	public void setStockId(long stock_id) {
		this.stockId = stock_id;
	}



	public String getKey() {
		return key;
	}



	public void setKey(String key) {
		this.key = key;
	}



	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}



	public int getTypeId() {
		return typeId;
	}



	public void setTypeId(int type_id) {
		this.typeId = type_id;
	}

	@Override
	public String toString() {
		return "StockPropertySource [stockId=" + stockId + ", key=" + key
				+ ", value=" + value + ", typeId=" + typeId + "]";
	}


}
