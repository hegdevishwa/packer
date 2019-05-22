package com.mobiquityinc.packer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.mobiquityinc.packer.Item;
import com.mobiquityinc.packer.Pack;

@RunWith(JUnit4.class)
public class PackTest {

	Pack pack;
	Item item1;
	Item item2;

	@Before
	public void setUp() {
		pack = new Pack();

		item1 = new Item();
		item1.setCost(10);
		item1.setIndex(1);
		item1.setWeight(10);

		item2 = new Item();
		item2.setCost(10);
		item2.setIndex(2);
		item2.setWeight(10);
	}

	@Test
	public void test_pack_constructor() {
		assertNotNull("Pack constructor initializes items list", pack.getItems());
	}

	@Test
	public void test_addItem() {
		pack.setMaxWeight(30);

		assertEquals("Test that addItem returns 1 after adding item", 1, pack.addItem(item1));
		assertEquals("Test that one item is added to items list", 1, pack.getItems().size());
		assertEquals("Test the total value of the pack after adding first item", 10, pack.getTotalValue(), 0);
		assertEquals("Test the total weight of the pack after adding first item", 10, pack.getTotalWeight(), 0);

		assertEquals("Test that addItem returns 1 after adding item", 1, pack.addItem(item2));
		assertEquals("Test that two item is added to items list", 2, pack.getItems().size());
		assertEquals("Test the total value of the pack is 20 after adding second item", 20, pack.getTotalValue(), 0);
		assertEquals("Test the total weight of the pack is 20 after adding second item", 20, pack.getTotalWeight(), 0);
	}

	@Test
	public void test_addItem_does_not_allow_more_than_max_weight_of_pack() {
		pack.setMaxWeight(10);

		assertEquals("Assert that addItem returns 1 after adding first item", 1, pack.addItem(item1));
		assertEquals("Assert that one item is added to items list", 1, pack.getItems().size());
		assertEquals("Assert the total value of the pack after adding first item", 10, pack.getTotalValue(), 0);
		assertEquals("Assert the total weight of the pack after adding first item", 10, pack.getTotalWeight(), 0);

		assertEquals("Assert that addItem returns 0 after adding second item", 0, pack.addItem(item2));
		assertNotEquals("Assert that second item is not added", 2, pack.getItems().size());
		assertNotEquals("Assert that total values is not updated", 20, pack.getTotalValue(), 0);
		assertNotEquals("Assert that total weight is not updated", 20, pack.getTotalWeight(), 0);
	}

	@Test
	public void test_getItemsIndexList() {
		pack.setMaxWeight(20);
		pack.addItem(item1);
		pack.addItem(item2);

		assertEquals("1,2", pack.getItemsIndexList());

		Pack pack2 = new Pack();
		assertEquals("Assert that if there are no items in pack", "-", pack2.getItemsIndexList());
	}

}
