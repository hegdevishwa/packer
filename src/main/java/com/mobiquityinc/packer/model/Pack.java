package com.mobiquityinc.packer.model;

import java.util.ArrayList;
import java.util.List;

public class Pack {

	private int maxWeight;
	private List<Item> items;
	private float totalWeight;
	private float totalValue;

	public Pack() {
		this.items = new ArrayList<>();
	}

	public Pack(int maxWeight, List<Item> items) {
		super();
		this.maxWeight = maxWeight;
		this.items = items;
	}

	public int getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(int maxWeight) {
		this.maxWeight = maxWeight;
	}

	public List<Item> getItems() {
		return items;
	}

	public float getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(float totalWeight) {
		this.totalWeight = totalWeight;
	}

	public float getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(float totalValue) {
		this.totalValue = totalValue;
	}

	public String getItemsIndexList() {
		StringBuffer sb = new StringBuffer();
		if (items.isEmpty()) {
			sb.append("-");
		}
		items.forEach(item -> {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(item.getIndex());
		});
		return sb.toString();
	}

	public int addItem(Item item) {
		if (maxWeight >= totalWeight + item.getWeight()) {
			this.items.add(item);
			this.totalWeight += item.getWeight();
			this.totalValue += item.getCost();
			return 1;
		} else {
			return 0;
		}

	}

}
