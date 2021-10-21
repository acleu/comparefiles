package com.als.comparefiles;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;

import static com.als.comparefiles.Criterion.*;
import static org.junit.jupiter.api.Assertions.*;

public class CriterionEvaluationTest {

	@Test
	public void testLocation() {
		assertEquals(
				FileName.getFingerprint(new FileEntry(new File("/root"), new File("/root/dir1/name.txt"), true), null),
				FileName.getFingerprint(new FileEntry(new File("/bottom"), new File("/bottom/dir2/name.txt"), false), null));
		assertEquals(
				FileDirectory.getFingerprint(new FileEntry(new File("/root"), new File("/root/dir1/dir2/name1.txt"), true), null),
				FileDirectory.getFingerprint(new FileEntry(new File("/bottom"), new File("/bottom/dir1/dir2/name2.txt"), false), null));
		assertEquals(
				FilePath.getFingerprint(new FileEntry(new File("/root"), new File("/root/dir1/dir2/name.txt"), true), null),
				FilePath.getFingerprint(new FileEntry(new File("/bottom"), new File("/bottom/dir1/dir2/name.txt"), false), null));

		assertNotEquals(
				FileName.getFingerprint(new FileEntry(new File("/root"), new File("/root/name1.txt"), true), null),
				FileName.getFingerprint(new FileEntry(new File("/root"), new File("/root/name2.txt"), false), null));
		assertNotEquals(
				FileDirectory.getFingerprint(new FileEntry(new File("/root"), new File("/root/dir1/dir2/name.txt"), true), null),
				FileDirectory.getFingerprint(new FileEntry(new File("/root"), new File("/root/dir1/dir3/name.txt"), false), null));
		assertNotEquals(
				FilePath.getFingerprint(new FileEntry(new File("/root"), new File("/root/dir1/dir2/name.txt"), true), null),
				FilePath.getFingerprint(new FileEntry(new File("/root"), new File("/root/dir1/dir3/name.txt"), false), null));
		assertNotEquals(
				FilePath.getFingerprint(new FileEntry(new File("/root"), new File("/root/dir1/dir2/name1.txt"), true), null),
				FilePath.getFingerprint(new FileEntry(new File("/root"), new File("/root/dir1/dir2/name2.txt"), false), null));
	}

	@Test
	public void testSize() {
		assertEquals(
				FileSize.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144.JPG"), true), null),
				FileSize.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144.JPG"), false), null));
		assertNotEquals(
				FileSize.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144.JPG"), true), null),
				FileSize.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00145.JPG"), false), null));
	}

	@Test
	public void testImageDimensionsOrFileSize() {
		assertEquals(
				new Dimension(2048, 1536),
				ImageDimensionsOrFileSize.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144.JPG"), true), null));
		assertEquals(
				592L,
				ImageDimensionsOrFileSize.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/Text1.txt"), true), null));
	}

	@Test
	public void testFileContents4k() {
		assertEquals(
				"n37Imrw17ooh/8OUzDvWLrFiSbI=",
				FileContents4k.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144.JPG"), true), null));
		assertEquals(
				FileContents4k.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144.JPG"), true), null),
				FileContents4k.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144_ChangedByteAfter8k.JPG"), false), null));
	}

	@Test
	public void testFileContentsAll() {
		assertEquals(
				"e+rhZU+RIuUzr+Nx3+QOhvcFe4w=",
				FileContentsAll.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144.JPG"), true), new Arguments()));
		assertNotEquals(
				FileContentsAll.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144.JPG"), true), new Arguments()),
				FileContentsAll.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144_ChangedByteAfter8k.JPG"), false), new Arguments()));
	}

	@Test
	public void testImagePixelsOrFileContents() {
		assertNotEquals( // content- and pixel-hashes are different
				FileContentsAll.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144.JPG"), true), new Arguments()),
				ImagePixelsOrFileContents.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144.JPG"), true), new Arguments()));
		assertEquals(
				"h0vO8WvtiqbjehMsRzJ7aqEpjrc=",
				ImagePixelsOrFileContents.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144.JPG"), true), new Arguments()));

		// different metadata but same pixel content

		assertEquals(
				ImagePixelsOrFileContents.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144.JPG"), true), new Arguments()),
				ImagePixelsOrFileContents.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144_MetadataChanged.JPG"), false), new Arguments()));
		assertNotEquals(
				FileContentsAll.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144.JPG"), true), new Arguments()),
				FileContentsAll.getFingerprint(new FileEntry(new File("./src/test/resources/"), new File("./src/test/resources/DSC00144_MetadataChanged.JPG"), false), new Arguments()));
	}
}
