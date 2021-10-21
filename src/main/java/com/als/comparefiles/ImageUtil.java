package com.als.comparefiles;

//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//import javax.imageio.metadata.IIOMetadata;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

class ImageUtil {

	static Dimension getImageDimension(final File imageFile) {

		try (ImageInputStream in = ImageIO.createImageInputStream(imageFile)) {
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
			if (readers.hasNext()) {
				final ImageReader reader = readers.next();
				try {
					reader.setInput(in);
					final int width = reader.getWidth(0);
					final int height = reader.getHeight(0);

					return new Dimension(Math.max(width, height), Math.min(width, height));
				} finally {
					reader.dispose();
				}
			}
			return null;
		} catch (final IOException e) {
			return null;
		}
	}

	static byte[] getImagePixels(final FileEntry entry) {

		try (ImageInputStream in = ImageIO.createImageInputStream(entry.file())) {
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
			if (readers.hasNext()) {
				final ImageReader reader = readers.next();

				try {
					reader.setInput(in);
//					final IIOMetadata metadata = reader.getImageMetadata(0);
//					final String[] names = metadata.getMetadataFormatNames();
//					for (String name : names) {
//						displayMetadata(metadata.getAsTree(name));
//					}

					final Raster raster = reader.readRaster(0, null);
					final DataBuffer dataBuffer = raster.getDataBuffer();
					if (dataBuffer instanceof DataBufferByte) {
						return ((DataBufferByte) dataBuffer).getData();
					}
				} finally {
					reader.dispose();
				}
			}
			return null;
		} catch (final IOException e) {
			return null;
		}
	}

	/*
	private static void displayMetadata(Node root) {
		displayMetadata(root, 0);
	}

	static void indent(int level) {
		for (int i = 0; i < level; i++)
			System.out.print("    ");
	}

	static void displayMetadata(Node node, int level) {
		// print open tag of element
		indent(level);
		System.out.print("<" + node.getNodeName());
		NamedNodeMap map = node.getAttributes();
		if (map != null) {

			// print attribute values
			int length = map.getLength();
			for (int i = 0; i < length; i++) {
				Node attr = map.item(i);
				System.out.print(" " + attr.getNodeName() +
						"=\"" + attr.getNodeValue() + "\"");
			}
		}

		Node child = node.getFirstChild();
		if (child == null) {
			// no children, so close element and return
			System.out.println("/>");
			return;
		}

		// children, so close current tag
		System.out.println(">");
		while (child != null) {
			// print children recursively
			displayMetadata(child, level + 1);
			child = child.getNextSibling();
		}

		// print close tag of element
		indent(level);
		System.out.println("</" + node.getNodeName() + ">");
	}
	 */
}
