package com.als.comparefiles;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.Consumer;

class FileFinder {
	static void findFiles(@NotNull final File parent, final Consumer<File> consumer) {

		if (parent.isFile()) {
			consumer.accept(parent);
			return;
		}

		final File[] files = parent.listFiles();
		if (files != null) { // null if no directory
			for (final File file : files) {
				findFiles(file, consumer);
			}
		}
	}
}
