package com.popina.product_management;

public class ProductContainer {
	int productId;
	String productName;
	double productCost;
	int productAvalibility;
	double productPreparationTime;
	
	public ProductContainer(String productName, double productCost,int productAvalibility,double productPreparationTime){
		this.productName = productName;
		this.productCost = productCost;
        this.productAvalibility=productAvalibility;
        this.productPreparationTime=productPreparationTime;
	}
	
	public ProductContainer(){
		this.productName="";
		this.productCost=0;
		this.productAvalibility=0;
		this.productPreparationTime=60;
	}
	
	
}
