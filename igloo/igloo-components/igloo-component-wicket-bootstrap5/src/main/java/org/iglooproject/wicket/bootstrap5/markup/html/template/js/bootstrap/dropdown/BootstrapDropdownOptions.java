package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.dropdown;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.options.ArrayItemOptions;
import org.wicketstuff.wiquery.core.options.IntegerOption;
import org.wicketstuff.wiquery.core.options.Options;

public class BootstrapDropdownOptions extends Options implements IBootstrapDropdownOptions {

	private static final long serialVersionUID = 904537124671042670L;

	public static final BootstrapDropdownOptions get() {
		return new BootstrapDropdownOptions();
	}

	public BootstrapDropdownOptions boundary(Boundary boundary) {
		Objects.requireNonNull(boundary);
		putLiteral("boundary", boundary.getValue());
		return this;
	}

	public BootstrapDropdownOptions boundary(JsStatement boundary) {
		Objects.requireNonNull(boundary);
		put("boundary", boundary.render(false).toString());
		return this;
	}

	public BootstrapDropdownOptions reference(Reference reference) {
		Objects.requireNonNull(reference);
		putLiteral("reference", reference.getValue());
		return this;
	}

	public BootstrapDropdownOptions reference(JsStatement reference) {
		Objects.requireNonNull(reference);
		put("reference", reference.render(false).toString());
		return this;
	}

	public BootstrapDropdownOptions display(Display display) {
		Objects.requireNonNull(display);
		putLiteral("display", display.getValue());
		return this;
	}

	public BootstrapDropdownOptions offset(int skidding, int distance) {
		put(
			"offset",
			List.of(skidding, distance)
				.stream()
				.map(IntegerOption::new)
				.collect(Collectors.toCollection(ArrayItemOptions<IntegerOption>::new))
		);
		return this;
	}

	public BootstrapDropdownOptions offset(String offset) {
		Objects.requireNonNull(offset);
		putLiteral("offset", offset);
		return this;
	}

	public BootstrapDropdownOptions offset(JsScope offset) {
		Objects.requireNonNull(offset);
		put("offset", offset);
		return this;
	}

	public BootstrapDropdownOptions autoClose(boolean autoClose) {
		Objects.requireNonNull(autoClose);
		put("autoClose", autoClose);
		return this;
	}

	public BootstrapDropdownOptions autoClose(AutoClose autoClose) {
		Objects.requireNonNull(autoClose);
		putLiteral("autoClose", autoClose.getValue());
		return this;
	}

	public enum Boundary {
		CLIPPING_PARENTS("clippingParents");
		
		private final String value;
		
		private Boundary(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

	public enum Reference {
		TOGGLE("toggle"),
		PARENT("parent");
		
		private final String value;
		
		private Reference(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

	public enum Display {
		DYNAMIC("dynamic"),
		STATIC("static");
		
		private final String value;
		
		private Display(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

	public enum AutoClose {
		INSIDE("inside"),
		OUTSIDE("outside");
		
		private final String value;
		
		private AutoClose(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

}
