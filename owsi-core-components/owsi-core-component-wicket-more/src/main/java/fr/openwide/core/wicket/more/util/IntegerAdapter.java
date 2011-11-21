package fr.openwide.core.wicket.more.util;

public class IntegerAdapter implements NumberAdapter<Integer> {
	private static final long serialVersionUID = -5051419302947871436L;

	public static final IntegerAdapter INSTANCE = new IntegerAdapter();

	@Override
	public Integer convert(Number number) {
		if (number == null) {
			return null;
		} else {
			return number.intValue();
		}
	}

	@Override
	public Class<Integer> getNumberClass() {
		return Integer.class;
	}
}