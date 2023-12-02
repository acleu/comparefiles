package com.als.comparefiles;

import lombok.val;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This test just if picocli works, i.e. if Arguments is annotated correctly.
 */
public class ArgumentsTest {

	public Arguments parse(String args) {
		val evaluatedArguments = new Arguments();
		val commandLine = new CommandLine(evaluatedArguments)
				.setCaseInsensitiveEnumValuesAllowed(true);
		System.out.println(commandLine.getUsageMessage());
		commandLine.parseArgs(args.split(" +"));
		return evaluatedArguments;
	}

	@Test
	public void testNone() {
		assertEquals(parse("-h"), new Arguments());
	}

	@Test
	public void testLocation() {
		assertEquals(parse("-l none"), new Arguments().withLocation(Arguments.Location.None));
		assertEquals(parse("-l none"), new Arguments().withLocation(Arguments.Location.None));
	}

	@Test
	public void testSize() {
		assertEquals(parse("-s"), new Arguments().withCompareSize(true));
	}

	@Test
	public void testContent() {
		assertEquals(parse("-c"), new Arguments().withContentLengthToCompare(Long.MIN_VALUE));
		assertEquals(parse("-c=100"), new Arguments().withContentLengthToCompare(100L));
		assertEquals(parse("-c1000"), new Arguments().withContentLengthToCompare(1000L));

		assertEquals(parse("-c1B"), new Arguments().withContentLengthToCompare(1L));
		assertEquals(parse("-c1KB"), new Arguments().withContentLengthToCompare(1024L));
		assertEquals(parse("-c1MB"), new Arguments().withContentLengthToCompare(1024L * 1024L));
		assertEquals(parse("-c1GB"), new Arguments().withContentLengthToCompare(1024L * 1024L * 1024L));
		assertEquals(parse("-c1TB"), new Arguments().withContentLengthToCompare(1024L * 1024L * 1024L * 1024L));
		assertEquals(parse("-c1PB"), new Arguments().withContentLengthToCompare(1024L * 1024L * 1024L * 1024L * 1024L));

		assertEquals(parse("-c 1b"), new Arguments().withContentLengthToCompare(1L));
		assertEquals(parse("-c 1kb"), new Arguments().withContentLengthToCompare(1024L));
		assertEquals(parse("-c 1mb"), new Arguments().withContentLengthToCompare(1024L * 1024L));
		assertEquals(parse("-c 1gb"), new Arguments().withContentLengthToCompare(1024L * 1024L * 1024L));
		assertEquals(parse("-c 1tb"), new Arguments().withContentLengthToCompare(1024L * 1024L * 1024L * 1024L));
		assertEquals(parse("-c 1pb"), new Arguments().withContentLengthToCompare(1024L * 1024L * 1024L * 1024L * 1024L));

		assertEquals(parse("-c1.6"), new Arguments().withContentLengthToCompare(1L));
		assertEquals(parse("-c1.6B"), new Arguments().withContentLengthToCompare(1L));
		assertEquals(parse("-c1.6b"), new Arguments().withContentLengthToCompare(1L));

		assertEquals(parse("-c1.6k"), new Arguments().withContentLengthToCompare((long) (1.6 * 1024L)));
		assertEquals(parse("-c1.6kB"), new Arguments().withContentLengthToCompare((long) (1.6 * 1024L)));

		assertEquals(parse("-c1.6m"), new Arguments().withContentLengthToCompare((long) (1.6 * 1024L * 1024L)));
		assertEquals(parse("-c1.6mB"), new Arguments().withContentLengthToCompare((long) (1.6 * 1024L * 1024L)));

		assertEquals(parse("-c1.6G"), new Arguments().withContentLengthToCompare((long) (1.6 * 1024L * 1024L * 1024L)));
		assertEquals(parse("-c1.6gB"), new Arguments().withContentLengthToCompare((long) (1.6 * 1024L * 1024L * 1024L)));

		assertEquals(parse("-c1.6T"), new Arguments().withContentLengthToCompare((long) (1.6 * 1024L * 1024L * 1024L * 1024L)));
		assertEquals(parse("-c1.6TB"), new Arguments().withContentLengthToCompare((long) (1.6 * 1024L * 1024L * 1024L * 1024L)));

		assertEquals(parse("-c1.6p"), new Arguments().withContentLengthToCompare((long) (1.6 * 1024L * 1024L * 1024L * 1024L * 1024L)));
		assertEquals(parse("-c1.6PB"), new Arguments().withContentLengthToCompare((long) (1.6 * 1024L * 1024L * 1024L * 1024L * 1024L)));
	}

	@Test
	public void testImageDimensions() {
		assertEquals(parse("-w"), new Arguments().withCompareImageDimensions(true));
	}

	@Test
	public void testImagePixels() {
		assertEquals(parse("-i"), new Arguments().withCompareImagePixels(true));
	}

	@Test
	public void testPaths() {
		assertEquals(parse("-h"), new Arguments()
				.withKeepPaths(new String[]{})
				.withRemovePaths(new String[]{}));
		assertEquals(parse("-kkeepPath"), new Arguments()
				.withKeepPaths(new String[]{"keepPath"})
				.withRemovePaths(new String[]{}));
		assertEquals(parse("-rremovePath"), new Arguments()
				.withKeepPaths(new String[]{})
				.withRemovePaths(new String[]{"removePath"}));

		assertThrows(CommandLine.UnmatchedArgumentException.class, () -> parse("-kkp1 kp2 -rrp1"));
		assertThrows(CommandLine.UnmatchedArgumentException.class, () -> parse("-kkp1 -rrp1 rp2"));

		assertEquals(parse("-kkp1 -kkp2 -rrp1 -rrp2 -k kp3 -r rp3"), new Arguments()
				.withKeepPaths(new String[]{"kp1", "kp2", "kp3"})
				.withRemovePaths(new String[]{"rp1", "rp2", "rp3"}));
	}
}
