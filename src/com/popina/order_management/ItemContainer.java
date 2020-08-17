package com.popina.order_management;

public class ItemContainer {
	int itemid;
	int quanitity;

	public ItemContainer(int itemId, int quanitity) {
		super();
		this.itemid = itemId;
		this.quanitity = quanitity;
	}

	public int getItemId() {
		return itemid;
	}

	public double getQuanitity() {
		return quanitity;
	}	

}
