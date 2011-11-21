package fr.openwide.core.wicket.more.util;

public class FloatAdapter implements NumberAdapter<Float> {
	private static final long serialVersionUID = -4217114946825987375L;

	public static final FloatAdapter INSTANCE = new FloatAdapter();

	@Override
	public Float convert(Number number) {
		if (number == null) {
			return null;
		} else {
			return number.floatValue();
		}
	}

	@Override
	public Class<Float> getNumberClass() {
		return Float.class;
	}
}
