package com.als.comparefiles;

import java.io.File;
import java.nio.file.Path;

record FileEntry(
		File searchRoot,
		File file,
		boolean keep
) {

	Path getRelativePath() {
		return searchRoot.toPath().relativize(file.toPath());
	}

	@Override
	public String toString() {
		return searchRoot + "//" + getRelativePath();
	}
}
