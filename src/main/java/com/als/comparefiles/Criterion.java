package com.als.comparefiles;

import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public enum Criterion {

	FileName() {
		public String getFingerprint(final FileEntry entry, Arguments arguments) {
			return entry.file().getName();
		}

		@Override
		public boolean isApplicable(Arguments arguments) {
			return arguments.getLocation() == Arguments.Location.Name;
		}
	},

	FileDirectory() {
		public String getFingerprint(final FileEntry entry, Arguments arguments) {
			final Path p = entry.getRelativePath().getParent();
			return p == null ? "" : p.toString();
		}

		@Override
		public boolean isApplicable(Arguments arguments) {
			return arguments.getLocation() == Arguments.Location.Dir;
		}
	},

	FilePath() {
		public Path getFingerprint(final FileEntry entry, Arguments arguments) {
			return entry.getRelativePath();
		}

		@Override
		public boolean isApplicable(Arguments arguments) {
			return arguments.getLocation() == Arguments.Location.Path;
		}
	},

	FileSize() {
		public Long getFingerprint(final FileEntry entry, Arguments arguments) {
			return entry.file().length();
		}

		@Override
		public boolean isApplicable(Arguments arguments) {
			return arguments.isCompareSize()
					|| arguments.getContentLengthToCompare() != null;
		}
	},

	ImageDimensionsOrFileSize() {
		public Object getFingerprint(final FileEntry entry, Arguments arguments) {
			final Dimension d = ImageUtil.getImageDimension(entry.file());
			if (d == null) {
				return FileSize.getFingerprint(entry, arguments);
			}
			return d;
		}

		@Override
		public boolean isApplicable(Arguments arguments) {
			return arguments.isCompareImageDimensions()
					|| arguments.isCompareImagePixels();
		}
	},

	FileContents4k() {
		public String getFingerprint(final FileEntry entry, Arguments arguments) {

			try (FileInputStream fis = new FileInputStream(entry.file())) {

				MessageDigest digest = MessageDigest.getInstance("SHA-1");

				final byte[] bb = new byte[4 * 1024];
				int read;
				if ((read = fis.read(bb, 0, bb.length)) >= 0) {
					digest.update(bb, 0, read);
				}

				return Base64.getEncoder().encodeToString(digest.digest());

			} catch (final Throwable e) {
				throw new RuntimeException(entry.toString(), e);
			}
		}

		@Override
		public boolean isApplicable(Arguments arguments) {
			return arguments.getContentLengthToCompare() != null;
		}
	},

	FileContentsAll() {
		public String getFingerprint(final FileEntry entry, Arguments arguments) {

			try (FileInputStream fis = new FileInputStream(entry.file())) {

				MessageDigest digest = MessageDigest.getInstance("SHA-1");

				final byte[] bb = new byte[128 * 1024];
				long readTotal = 0;
				int read;
				while ((read = fis.read(bb, 0, bytesToRead(bb.length, readTotal, arguments))) >= 0) {
					digest.update(bb, 0, read);
					readTotal += read;
				}

				return Base64.getEncoder().encodeToString(digest.digest());

			} catch (final Throwable e) {
				throw new RuntimeException(entry.toString(), e);
			}

		}

		int bytesToRead(int maxRead, long readTotal, Arguments arguments) {

			if (arguments.getContentLengthToCompare() == null
					|| arguments.getContentLengthToCompare() == Long.MIN_VALUE)
				return maxRead;

			long bytesToReadTotal = Objects.requireNonNullElse(arguments.getContentLengthToCompare(), Long.MAX_VALUE) - readTotal;


			return (int) Math.max(0, Math.min(maxRead, bytesToReadTotal));
		}

		@Override
		public boolean isApplicable(Arguments arguments) {
			return arguments.getContentLengthToCompare() != null;
		}
	},

	ImagePixelsOrFileContents() {
		public Object getFingerprint(final FileEntry entry, Arguments arguments) {
			try {
				return Base64.getEncoder().encodeToString(
						MessageDigest.getInstance("SHA-1").digest(
								ImageUtil.getImagePixels(entry)));
			} catch (final RuntimeException e) {
				if (!(e instanceof NullPointerException)) {
					throw e;
				}
				return FileContentsAll.getFingerprint(entry, arguments);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public boolean isApplicable(Arguments arguments) {
			return arguments.isCompareImagePixels();
		}
	};

	public abstract Object getFingerprint(final FileEntry entry, Arguments arguments);

	public abstract boolean isApplicable(Arguments arguments);

	@NotNull
	public static List<Criterion> determineCriteria(@NotNull final Arguments arguments) {

		val criteria = Arrays.stream(Criterion.values())
				.filter(c -> c.isApplicable(arguments))
				.sorted()
				.distinct()
				.toList();

		return !criteria.isEmpty()
				? criteria
				: List.of(Criterion.FilePath, Criterion.FileSize, Criterion.FileContents4k, Criterion.FileContentsAll);
	}
}
