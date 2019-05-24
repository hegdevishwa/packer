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

		// LinkedHashMap maintains the insertion order. This will allow us to print the
		// result in order of input
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
			// Read lines
			lines = Files.readAllLines(Paths.get(filePath));
		} catch (IOException e) {
			throw new APIException("No such file found", e);
		}

		for (String line : lines) {
			// Validate input
			validateInput(line);
			// Remove input separator characters
			line = removeSeperators(line);

			//Split the line at ":" to extract max weight and input items
			String[] arr = line.split(":");
			int maxPackWeight = (Integer.parseInt(arr[0].replaceAll("\\s+", "")));

			List<Item> items = new ArrayList<>();
			//Split the input items at space to get values for each items
			String[] inputValues = arr[1].split("\\s+");

			for (String str : inputValues) {
				if (!str.isEmpty()) {
					Item item = createItem(maxPackWeight, str);

					// Check if the weight of the item is less than allowed weight of the pack
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
	 * @return a list of packages chosen based on above criteria
	 */
	private static List<Pack> perparePackages(Map<Integer, List<Item>> packageItemMap) {

		ArrayList<Pack> finalConsignment = new ArrayList<>();
		packageItemMap.forEach((weight, items) -> {

			//Sort the items to pick the item of highest value
			sortItems(items);

			List<Pack> passiblePacks = new ArrayList<>();
			Pack pack;
			//Try different combinations of pack and store the values
			for (int i = 0; i < items.size(); i++) {
				pack = new Pack();
				pack.setMaxWeight(weight);
				pack.addItem(items.get(i));
				for (int j = i + 1; j < items.size(); j++) {
					pack.addItem(items.get(j));
				}
				passiblePacks.add(pack);
			}

			//Sort all the possible packs to pick the pack containing items of maximum value
			sortPacks(passiblePacks);

			if (passiblePacks.size() > 0) {
				finalConsignment.add(passiblePacks.get(0));
			} else {
				//If there is no pack available then set a pack with no items in it.
				pack = new Pack();
				pack.setMaxWeight(weight);
				finalConsignment.add(pack);
			}
		});
		return finalConsignment;
	}

	/**
	 * Validates the input string is according to spec. Throws APIExceptin if the
	 * string is not valid
	 * 
	 * @param String
	 *            line to be validated
	 * @throws APIException
	 */
	private static void validateInput(String line) throws APIException {
		String regex = "\\d+\\s:(\\s\\(\\d+,\\d+\\.*\\d*,(\\u20AC)\\d+\\.*\\d*\\))+";
		if (!line.matches(regex)) {
			throw new APIException("Invalid input in line: " + line);
		}
	}

	/**
	 * Removes the input separator characters
	 * 
	 * @param line
	 * @return
	 */
	private static String removeSeperators(String line) {
		return line.replaceAll("[()(\\u20AC)]", "");
	}

	/**
	 * Sorts the items list in descending order of their cost. If there are more
	 * than one item of same cost then choose the one with the less weight. This way
	 * we always choose the first item with highest cost and if there are multiple
	 * items of same cost we choose an item with highest cost but least weight among
	 * those items.
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
	 * Sorts all the possible packs in descending order of their total value. If
	 * there are more than one items with same total value, then choose the one with
	 * the lest total weight. This way we always choose the first pack with the
	 * highest total value and if there are multiple packs of the same value we
	 * still choose an item with highest value but with least weight among those
	 * available combination of packs.
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
