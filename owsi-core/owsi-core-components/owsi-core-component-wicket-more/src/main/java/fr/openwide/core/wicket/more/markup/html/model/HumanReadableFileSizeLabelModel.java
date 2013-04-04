package fr.openwide.core.wicket.more.markup.html.model;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.google.common.collect.Lists;

public class HumanReadableFileSizeLabelModel extends LoadableDetachableModel<String> {

	private static final long serialVersionUID = 2983505085932967800L;

	private static final double BYTES_IN_KB = 1024;

	private static final double DISPLAY_LIMIT = 1000;

	private static final String BYTE_UNIT_KEY = "HumanReadableFileSize.byte";
	private static final String KB_UNIT_KEY = "HumanReadableFileSize.kiloByte";
	private static final String MB_UNIT_KEY = "HumanReadableFileSize.megaByte";
	private static final String GB_UNIT_KEY = "HumanReadableFileSize.gigaByte";
	private static final String TB_UNIT_KEY = "HumanReadableFileSize.terraByte";

	private static final String OUTPUT_DECIMAL_FORMAT = "#.#";

	private static final List<String> UNIT_KEYS = Lists.newArrayList(BYTE_UNIT_KEY, KB_UNIT_KEY, MB_UNIT_KEY,
			GB_UNIT_KEY, TB_UNIT_KEY);

	private IModel<Long> sizeInByteModel;

	private Component component;

	private boolean returnZero;

	public HumanReadableFileSizeLabelModel(IModel<Long> sizeInByteModel, Component component) {
		this(sizeInByteModel, component, true);
	}
	
	public HumanReadableFileSizeLabelModel(IModel<Long> sizeInByteModel, Component component, boolean returnZero) {
		super();
		
		this.sizeInByteModel = sizeInByteModel;
		this.component = component;
		this.returnZero = returnZero;
	}

	@Override
	protected String load() {
		Long sizeInByte = sizeInByteModel.getObject();
		
		if (sizeInByte != null) {
			double humanReadableSize = sizeInByte.doubleValue();
			int unitKeyIndex = 0;
			
			while (humanReadableSize > DISPLAY_LIMIT && unitKeyIndex < UNIT_KEYS.size() - 1) {
				humanReadableSize = humanReadableSize / BYTES_IN_KB;
				unitKeyIndex++;
			}
			
			DecimalFormat twoDecimalsFormat = new DecimalFormat(OUTPUT_DECIMAL_FORMAT);
			
			if (returnZero || humanReadableSize != 0) {
				return new StringBuilder(twoDecimalsFormat.format(humanReadableSize)).append(" ")
						.append(component.getString(UNIT_KEYS.get(unitKeyIndex)))
						.toString();
			}
		}
		return null;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		if (sizeInByteModel != null) {
			sizeInByteModel.detach();
		}
	}
}
