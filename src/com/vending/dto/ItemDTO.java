package com.vending.dto;

import java.io.Serializable;

public class ItemDTO implements Serializable {

	private String name;
	private double cost;
	private int invAmount;
	
	public ItemDTO() {}

	public ItemDTO(String name, double cost, int invAmount) {
		super();
		this.name = name;
		this.cost = cost;
		this.invAmount = invAmount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getInvAmount() {
		return invAmount;
	}

	public void setInvAmount(int invAmount) {
		this.invAmount = invAmount;
	}

	@Override
	public String toString() {
		return "ItemDTO [name: " + name + "\t| cost: $" + String.format("%.2f", cost) + "]" + "inv=" + invAmount;
	}
}
