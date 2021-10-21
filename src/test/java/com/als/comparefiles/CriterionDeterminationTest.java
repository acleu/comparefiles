package com.als.comparefiles;

import lombok.val;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.util.Collection;
import java.util.List;

import static com.als.comparefiles.Criterion.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CriterionDeterminationTest {

	public Collection<Criterion> determineCriteria(String args) {
		val evaluatedArguments = new Arguments();
		if (args != null) new CommandLine(evaluatedArguments).parseArgs(args.split(" +"));
		return Criterion.determineCriteria(evaluatedArguments);
	}

	@Test
	public void testDefault() {
		assertEquals(determineCriteria(null),
				List.of(FilePath, FileSize, FileContents4k, FileContentsAll));
	}

	@Test
	public void testLocation() {
		assertEquals(determineCriteria("-lNone"), List.of(FilePath, FileSize, FileContents4k, FileContentsAll));
		assertEquals(determineCriteria("-lName"), List.of(FileName));
		assertEquals(determineCriteria("-lDir"), List.of(FileDirectory));
		assertEquals(determineCriteria("-lPath"), List.of(FilePath));
	}

	@Test
	public void testSize() {
		assertEquals(determineCriteria("-s"), List.of(FileSize));
	}

	@Test
	public void testContent() {
		assertEquals(determineCriteria("-c"), List.of(FileSize, FileContents4k, FileContentsAll));
		assertEquals(determineCriteria("-c100"), List.of(FileSize, FileContents4k, FileContentsAll));
	}

	@Test
	public void testImageDimensions() {
		assertEquals(determineCriteria("-w"), List.of(ImageDimensionsOrFileSize));
	}

	@Test
	public void testImagePixels() {
		assertEquals(determineCriteria("-i"), List.of(ImageDimensionsOrFileSize, ImagePixelsOrFileContents));
	}
}
