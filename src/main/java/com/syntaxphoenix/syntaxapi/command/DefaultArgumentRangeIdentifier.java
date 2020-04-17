package com.syntaxphoenix.syntaxapi.command;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.syntaxphoenix.syntaxapi.command.range.*;
import com.syntaxphoenix.syntaxapi.reflections.ClassCache;
import com.syntaxphoenix.syntaxapi.utils.java.Strings;

/**
 * @author Lauriichen
 *
 */
public class DefaultArgumentRangeIdentifier extends ArgumentRangeIdentifier {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ArrayList<BaseArgumentRange> process(String... ranges) {
		ArrayList<BaseArgumentRange> list = new ArrayList<>();
		if (ranges == null || ranges.length == 0) {
			return list;
		}
		for (String range : ranges) {
			if (range.startsWith("collection::")) {
				String[] part = (range = range.replaceFirst("collection::", "")).split("[", 2);

				String type = part[0];
				Class<?> valueType;
				if ((valueType = ClassCache.getClass(type)) == null)
					continue;

				part = part[1].split("\\,", 2);
				if (!(Strings.isNumeric(part[0]) && Strings.isNumeric(part[1])))
					continue;

				int min, max;

				try {
					min = Integer.parseInt(part[0]);
					max = Integer.parseInt(part[1].substring(0, part[1].length() - 1));
				} catch (NumberFormatException ignore) {
					continue;
				}
				list.add(new CollectionSizeRange(min, max, valueType));
			} else if (range.startsWith("number[")) {
				String[] part = (range = range.replaceFirst("number[", "")).split("\\,", 2);
				if (!((Strings.isNumeric(part[0]) || Strings.isDecimal(part[0]))
						&& (Strings.isNumeric(part[1]) || Strings.isDecimal(part[1]))))
					continue;

				BigDecimal min, max;

				try {
					min = new BigDecimal(part[0]);
					max = new BigDecimal(part[1].substring(0, part[1].length() - 1));
				} catch (NumberFormatException ignore) {
					continue;
				}
				list.add(new NumberValueRange(min, max));
			} else if (range.startsWith("text[")) {
				String[] part = (range = range.replaceFirst("text[", "")).split("\\,", 2);
				if (!(Strings.isNumeric(part[0]) && Strings.isNumeric(part[1])))
					continue;

				int min, max;

				try {
					min = Integer.parseInt(part[0]);
					max = Integer.parseInt(part[1].substring(0, part[1].length() - 1));
				} catch (NumberFormatException ignore) {
					continue;
				}
				list.add(new TextSizeRange(min, max));
			} else if (range.startsWith("state[")) {
				if((range = range.replaceFirst("state[", "")).length() == 1) {
					list.add(new StateRange());
					continue;
				}
				if(!Strings.isBoolean(range = range.substring(0, range.length() - 1)))
					continue;
				list.add(new StateRange(Boolean.valueOf(range)));
			}
		}

		return null;
	}

	@Override
	public String[] asStringArray(BaseArgumentRange... ranges) {
		if (ranges == null || ranges.length == 0) {
			return new String[0];
		}
		int length = ranges.length;
		String[] array = new String[length];
		for (int index = 0; index < length; index++) {
			array[index] = ranges[index].toString();
		}
		return array;
	}

}
