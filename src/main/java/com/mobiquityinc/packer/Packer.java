package com.mobiquityinc.packer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.mobiquityinc.exception.APIException;

public class Packer {

	public static String pack(String filePath) {

		// LinkedHashMap maintains the insertion order. This will help in getting the
		// output as in the spec.
		Map<Integer, List<Item>> packageItemMap = parseInput(filePath);

		List<Pack> finalConsignment = perparePackage(packageItemMap);
		StringBuffer sb = new StringBuffer();

		for (Pack pack : finalConsignment) {
			sb.append(pack.getItemsIndexList());
			sb.append("\n");
		}

		System.out.println(sb.toString());
		return null;
	}

	private static List<Pack> perparePackage(Map<Integer, List<Item>> packageItemMap) {

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

	private static Map<Integer, List<Item>> parseInput(String filePath) {

		Map<Integer, List<Item>> packageItemMap = new LinkedHashMap<>();

		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {

			stream.map(Packer::validateInput).map(Packer::removeSeperators).forEach((line) -> {
				String[] arr1 = line.split(":");
				int maxPackWeight = (Integer.parseInt(arr1[0].replaceAll("\\s+", "")));

				List<Item> items = new ArrayList<>();
				String[] strArr = arr1[1].split("\\s+");

				for (String str : strArr) {
					if (!str.isEmpty()) {
						Item item = new Item();
						String[] values = str.split(",");
						item.setIndex(Integer.valueOf(values[0]));
						item.setWeight(Float.parseFloat(values[1]));
						item.setCost(Float.parseFloat(values[2]));
						if (item.getWeight() <= maxPackWeight) {
							items.add(item);
						}
					}
				}
				packageItemMap.put(maxPackWeight, items);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return packageItemMap;
	}

	private static String validateInput(String line) {
		System.out.println(line);
		String regex = "\\d+\\s:(\\s\\(\\d+,\\d+.\\d+,(\\\\u20AC)\\d+\\))+";
		System.out.println(line.matches(regex));

		if (!line.matches(regex)) {
			throw new APIException("Invalid input");
		}
		return line;
	}

	private static String removeSeperators(String line) {
		return line.replaceAll("[()€]", "");
	}

	private static void sortItems(List<Item> items) {
		items.sort((Item i1, Item i2) -> {
			return (int) Math.signum(i2.getCost() - i1.getCost());
		});

	}

	private static void sortPacks(List<Pack> packs) {
		packs.sort((Pack p1, Pack p2) -> {
			int order = (int) Math.signum(p2.getTotalValue() - p1.getTotalValue());
			if (order == 0) {
				order = (int) Math.signum(p1.getTotalWeight() - p2.getTotalWeight());
			}
			return order;
		});
	}

	public static void main(String[] args) throws IOException {
		pack("C:\\Workspace\\packer\\packer\\src\\main\\java\\resource\\ip");

	}
}
