package com.als.comparefiles;

import picocli.CommandLine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CompareFiles {

	public static void main(final String[] args) {

		final long start = System.currentTimeMillis();

		// parse arguments ------------------------------------------------------------------------

		int exitCode = new CommandLine(
				new Arguments() {
					@Override
					public Integer call() {

						// execute ----------------------------------------------------------------
						execute(this);
						return 0;
					}
				}
		).setCaseInsensitiveEnumValuesAllowed(true)
				.execute(args);


		// log timing -----------------------------------------------------------------------------

		final long stop = System.currentTimeMillis();
		System.out.println("Needed " + (stop - start) + " ms");

		System.exit(exitCode);
	}

	public static void execute(final Arguments arguments) {

		// find the files -------------------------------------------------------------------------

		System.out.println("Crawling...");

		final List<FileEntry> fileEntries = new ArrayList<>();

		for (String path : arguments.getKeepPaths()) {
			final File searchRoot = new File(path);
			FileFinder.findFiles(searchRoot,
					file -> fileEntries.add(
							new FileEntry(searchRoot, file, true)));
		}

		for (String path : arguments.getRemovePaths()) {
			final File searchRoot = new File(path);
			FileFinder.findFiles(searchRoot,
					file -> fileEntries.add(
							new FileEntry(searchRoot, file, false)));
		}

		// split buckets by criteria and filter out non-duplicates --------------------------------

		System.out.println("Building pipeline");

		Stream<FileEntryBucket> bucketStream = Stream.of(new FileEntryBucket(fileEntries));

		for (final Criterion criterion : Criterion.determineCriteria(arguments)) {

			System.out.println("  Adding " + criterion);

			bucketStream =
					bucketStream
							.flatMap(fileEntryBucket -> fileEntryBucket.split(criterion, arguments)) // TODO check if each criterion is evaluated only once per file max
							.filter(FileEntryBucket::hasDuplicates);
		}

		// delete duplicates ----------------------------------------------------------------------

		System.out.println("Evaluating");

		bucketStream.forEach(fileEntryBucket -> {

			final Map<Boolean, List<FileEntry>> originalsAndDuplicates = fileEntryBucket.getOriginalsAndDuplicates();

			final List<FileEntry> originals = originalsAndDuplicates.get(true);
			final List<FileEntry> duplicates = originalsAndDuplicates.get(false);

			System.out.println("----------------------------");
			originals.forEach(fileEntry -> System.out.println("Original    " + fileEntry.searchRoot() + "//\t" + fileEntry.getRelativePath()));
			duplicates.forEach(fileEntry -> System.out.println("  Duplicate " + fileEntry.searchRoot() + "//\t" + fileEntry.getRelativePath() +
					((!fileEntry.keep() && fileEntry.file().delete())
							? " deleted "
							: " not deleted")));
		});
	}
}
