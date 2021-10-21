package com.als.comparefiles;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.partitioningBy;

record FileEntryBucket(
		List<FileEntry> fileEntries) {

	Stream<FileEntryBucket> split(final Criterion criterion, Arguments arguments) {

		return fileEntries.stream()
				.collect(groupingBy(e -> criterion.getFingerprint(e, arguments)))
				.values().stream()
				.map(FileEntryBucket::new);
	}

	Map<Boolean, List<FileEntry>> getOriginalsAndDuplicates() {
		return fileEntries.stream()
				.collect(partitioningBy(FileEntry::keep));
	}

	boolean hasDuplicates() {
		return fileEntries.size() > 1;
	}
}
