package com.mobiquityinc.packer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mobiquityinc.exception.APIException;

public class Packer {

	public static String pack(String filePath) throws APIException {

		// LinkedHashMap maintains the insertion order.
		Map<Integer, List<Item>> packageItemMap = parseInput(filePath);

		List<Pack> finalConsignment = perparePackages(packageItemMap);
		StringBuffer sb = new StringBuffer();

		for (Pack pack : finalConsignment) {
			sb.append(pack.getItemsIndexList());
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * Parses the input file and extracts the values.
	 * 
	 * @param filePath
	 * @return a LinkedHashMap of max allowed weight for a pack as key and a list
	 *         items to put in pack.
	 */
	private static Map<Integer, List<Item>> parseInput(String filePath) throws APIException {

		Map<Integer, List<Item>> packageItemMap = new LinkedHashMap<>();

		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(filePath));
		} catch (IOException e) {
			throw new APIException("No such file found", e);
		}

		for (String line : lines) {
			validateInput(line);
			line = removeSeperators(line);

			String[] arr1 = line.split(":");
			int maxPackWeight = (Integer.parseInt(arr1[0].replaceAll("\\s+", "")));

			List<Item> items = new ArrayList<>();
			String[] strArr = arr1[1].split("\\s+");

			for (String str : strArr) {
				if (!str.isEmpty()) {
					Item item = createItem(maxPackWeight, str);

					// Check if the weight of the item is less than allowed weight of th pack
					// if not skip the item
					if (item.getWeight() <= maxPackWeight) {
						items.add(item);
					}
				}

			}
			packageItemMap.put(maxPackWeight, items);
		}

		return packageItemMap;
	}

	/**
	 * Prepares packages with items for final consignment. Packages are chosen based
	 * on the value. Package of highest value is chosen. If there are more than two
	 * packages of same value package with less weight is chosen to put in final
	 * consignment
	 * 
	 * @param packageItemMap
	 * @return a list of packeges chosen based on above criteria
	 */
	private static List<Pack> perparePackages(Map<Integer, List<Item>> packageItemMap) {

		ArrayList<Pack> finalConsignment = new ArrayList<>();
		packageItemMap.forEach((weight, items) -> {

			sortItems(items);

			List<Pack> passiblePacks = new ArrayList<>();
			Pack pack;
			for (int i = 0; i < items.size(); i++) {
				pack = new Pack();
				pack.setMaxWeight(weight);
				pack.addItem(items.get(i));
				for (int j = i + 1; j < items.size(); j++) {
					pack.addItem(items.get(j));
				}
				passiblePacks.add(pack);
			}

			sortPacks(passiblePacks);

			if (passiblePacks.size() > 0) {
				finalConsignment.add(passiblePacks.get(0));
			} else {
				finalConsignment.add(new Pack());
			}
		});
		return finalConsignment;
	}

	/**
	 * Validates the input string is according to spec. Throws APIExceptin if the
	 * string is not valid
	 * 
	 * @param String line to be validated
	 * @throws APIException
	 */
	private static void validateInput(String line) throws APIException {
		String regex = "\\d+\\s:(\\s\\(\\d+,\\d+\\.*\\d*,(\\u20AC)\\d+\\.*\\d*\\))+";
		if (!line.matches(regex)) {
			throw new APIException("Invalid input");
		}
	}

	/**
	 * Removes the input seperator characters
	 * 
	 * @param line
	 * @return
	 */
	private static String removeSeperators(String line) {
		return line.replaceAll("[()€]", "");
	}

	/**
	 * Sorts the items list in descending order of their cost. If there are two
	 * items of the same cost then choose one with the less weight
	 * 
	 * @param items
	 */
	private static void sortItems(List<Item> items) {
		items.sort((Item i1, Item i2) -> {
			int order = (int) Math.signum(i2.getCost() - i1.getCost());
			if (order == 0) {
				order = (int) Math.signum(i1.getWeight() - i2.getWeight());
			}
			return order;
		});
	}

	/**
	 * Sorts all the psooible packs in descending order of their cost. If there are
	 * two items of the same cost then choose one with the less weight
	 * 
	 * @param items
	 */
	private static void sortPacks(List<Pack> packs) {
		packs.sort((Pack p1, Pack p2) -> {
			int order = (int) Math.signum(p2.getTotalValue() - p1.getTotalValue());
			if (order == 0) {
				order = (int) Math.signum(p1.getTotalWeight() - p2.getTotalWeight());
			}
			return order;
		});
	}

	private static Item createItem(int maxPackWeight, String str) {
		Item item = new Item();
		String[] values = str.split(",");
		item.setIndex(Integer.valueOf(values[0]));
		item.setWeight(Float.parseFloat(values[1]));
		item.setCost(Float.parseFloat(values[2]));
		return item;
	}

}
