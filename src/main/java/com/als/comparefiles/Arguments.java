package com.als.comparefiles;

import lombok.*;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Getter
@With
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Command(name = "cf",
		version = "cf 1.0.1-SNAPSHOT",
		sortOptions = false,
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		usageHelpAutoWidth = true,
		usageHelpWidth = 200,
		description = "Searches for duplicate files an deletes duplicates in remove paths.")
public class Arguments implements Callable<Integer> {

	// Comparison by location  ------------------------------------------------

	public enum Location {
		None, Name, Dir, Path /* Dir + Name */
	}

	@Option(names = {"-l", "--location"}, description = "speØcifies which part of the relative pathname should be compared. Possible values: ${COMPLETION-CANDIDATES}")
	private Location location;

	// Comparison by content  -------------------------------------------------

	@Option(names = {"-s", "--size"}, description = "compare file sizes")
	private boolean compareSize;

	@Option(names = {"-c", "--content"},
			arity = "0..1",
			fallbackValue = "" + Long.MIN_VALUE, // MIN_Value signals: compare whole file
			converter = SizeConverter.class,
			description = "compare files based on byte content up to contentLengthToCompare bytes, accepts units B/KB/MB/GB/TB/PB, implies -s")
	private Long contentLengthToCompare = null;

	@Option(names = {"-w", "--imageDimensions"}, description = "compare images based on width and height, implies -s for non-images")
	private boolean compareImageDimensions;

	@Option(names = {"-i", "--image"}, description = "compare images based on pixels, implies -w for images and -sc for non-images")
	private boolean compareImagePixels;

	// Paths to search --------------------------------------------------------

	@Option(names = {"-k", "--keepPaths"},
			arity = "1..*",
			description = "all following parameters are paths where originals are located, which should be kept")
	private String[] keepPaths = {};

	@Option(names = {"-r", "--removePaths"},
			arity = "1..*",
			description = "all following parameters are paths where removable duplicates are located")
	private String[] removePaths = {};

	/**
	 * A dummy implementation
	 */
	@Override
	public Integer call() throws Exception {
		return 0;
	}
}
