package com.als.comparefiles;

import picocli.CommandLine.ITypeConverter;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SizeConverter implements ITypeConverter<Long> {

	@Override
	public Long convert(String sizeString) {
		final Pattern pattern = Pattern.compile("^(-?[\\d.]+)\\s*(([PTGMK]?B?)?)$", Pattern.CASE_INSENSITIVE);
		final Matcher matcher = pattern.matcher(sizeString);
		if (!matcher.find())
			throw new NumberFormatException(sizeString + " is no size");

		BigDecimal number = new BigDecimal(matcher.group(1));
		final String unit = matcher.group(2).toUpperCase();

		final BigDecimal k = new BigDecimal(1024);

		switch (unit) {
			case "PB":
			case "P":
				number = number.multiply(k);
			case "TB":
			case "T":
				number = number.multiply(k);
			case "GB":
			case "G":
				number = number.multiply(k);
			case "MB":
			case "M":
				number = number.multiply(k);
			case "KB":
			case "K":
				number = number.multiply(k);
			case "B":
			case "":
				break;

			default:
				throw new IllegalArgumentException("Could not parse " + sizeString);
		}

		return number.longValue();
	}
}
