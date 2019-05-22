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
	public int compareTo(Item o) {
		if (this.getCost() < o.getCost()) {
			return -1;
		} else if (this.getCost() > o.getCost()) {
			return 1;
		}
		return 0;
	}

}
