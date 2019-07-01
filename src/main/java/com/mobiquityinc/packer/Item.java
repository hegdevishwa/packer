package com.mobiquityinc.packer;

public class Item implements Comparable<Item> {

	int index;
	float weight;
	float cost;

	public Item() {

	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	@Override
	public int compareTo(Item that) {
		int order = (int) Math.signum(that.getCost() - this.getCost());
		if (order == 0) {
			order = (int) Math.signum(this.getWeight() - that.getWeight());
		}
		return order;
	}

}
