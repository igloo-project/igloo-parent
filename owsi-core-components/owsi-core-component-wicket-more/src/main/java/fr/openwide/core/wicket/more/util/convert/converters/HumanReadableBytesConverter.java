package fr.openwide.core.wicket.more.util.convert.converters;

import java.util.List;

import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.rendering.BytesRenderer;

/**
 * @deprecated Use {@link BytesRenderer} instead. Be careful, though: the resource keys for the unit abbreviations are
 * not the same, and the output may thus change if you customized the abbreviations or translated them.
 */
@Deprecated
public class HumanReadableBytesConverter extends BytesRenderer {
	
	private static final long serialVersionUID = -5011963259015482288L;

	private static final String BYTE_UNIT_KEY = "HumanReadableBytesConverter.byte";
	private static final String KB_UNIT_KEY = "HumanReadableBytesConverter.kilobyte";
	private static final String MB_UNIT_KEY = "HumanReadableBytesConverter.megabyte";
	private static final String GB_UNIT_KEY = "HumanReadableBytesConverter.gigabyte";
	private static final String TB_UNIT_KEY = "HumanReadableBytesConverter.terabyte";

	private static final List<String> UNIT_KEYS = Lists.newArrayList(BYTE_UNIT_KEY, KB_UNIT_KEY, MB_UNIT_KEY, GB_UNIT_KEY, TB_UNIT_KEY);

	public HumanReadableBytesConverter() {
		super(UNIT_KEYS);
	}
}
