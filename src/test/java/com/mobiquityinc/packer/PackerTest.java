package com.mobiquityinc.packer;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mobiquityinc.exception.APIException;

public class PackerTest {

	private static String validInputFile = "D:\\open-repo\\packer\\src\\test\\java\\resource\\valid-input-file.txt";
	private static String invalidInputFile = "D:\\open-repo\\packer\\src\\test\\java\\resource\\invalid-input-file.txt";

	@Test
	public void testPack_with_valid_input_format() throws APIException {
		String expected = "4\n" + "-\n" + "2,7\n" + "8,9\n";
		String actual = Packer.pack(validInputFile);
		System.out.println(actual);
		assertEquals(expected, actual);
	}

	@Test(expected = APIException.class)
	public void testPack_with_invalid_input_format() throws APIException {
		Packer.pack(invalidInputFile);
	}

}
