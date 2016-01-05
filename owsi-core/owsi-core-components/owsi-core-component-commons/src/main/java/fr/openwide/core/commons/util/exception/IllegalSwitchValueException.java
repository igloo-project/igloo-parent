package fr.openwide.core.commons.util.exception;

public class IllegalSwitchValueException extends IllegalStateException {

	private static final long serialVersionUID = -6115037077521374684L;

	public IllegalSwitchValueException(Object value) {
		super(String.format(
				"Value '%s' of type '%s' had unexpected value when reaching a switch/case.",
				value, value == null ? null : value.getClass()
		));
	}

}
