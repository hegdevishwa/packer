package com.mobiquityinc.packer;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.junit.Test;

import com.mobiquityinc.exception.APIException;

public class PackerTest {

	private static String validInputFile = "resource/valid-input-file.txt";
	private static String invalidInputFile = "resource/invalid-input-file.txt";

	@Test
	public void testPack_with_valid_input_format() throws APIException, URISyntaxException {
		String expected = "4\n" + "-\n" + "2,7\n" + "8,9\n";
		String actual = Packer.pack(getAbsolutePath(validInputFile));
		System.out.println(actual);
		assertEquals(expected, actual);
	}

	@Test(expected = APIException.class)
	public void testPack_with_invalid_input_format() throws APIException, URISyntaxException {
		Packer.pack(getAbsolutePath(invalidInputFile));
	}
	
	@Test(expected = APIException.class)
	public void testPack_no_file_available() throws APIException {
		Packer.pack("fileDoesNotExist");
	}

	private String getAbsolutePath(String relativePath) throws URISyntaxException {
		URL res = getClass().getClassLoader().getResource(relativePath);
		File file = Paths.get(res.toURI()).toFile();
		String absolutePath = file.getAbsolutePath();
		return absolutePath;
	}

}
