package igloo.bootstrap.jsmodel;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import igloo.bootstrap.js.statement.IJsFunction;
import igloo.bootstrap.js.statement.IJsLiteral;
import igloo.bootstrap.js.statement.IJsNumber;
import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.js.statement.IJsString;
import igloo.bootstrap.js.statement.JsBoolean;
import igloo.bootstrap.js.statement.JsFunction;
import igloo.bootstrap.js.statement.JsLiteral;
import igloo.bootstrap.js.statement.JsMapping;
import igloo.bootstrap.js.statement.JsNumber;
import igloo.bootstrap.js.statement.JsSequence;
import igloo.bootstrap.js.statement.JsString;
import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.js.type.JsBooleanType;
import igloo.bootstrap.js.type.JsElementType;
import igloo.bootstrap.js.type.JsNumberType;
import igloo.bootstrap.js.type.JsStringType;

public class JsHelpers {

	private JsHelpers() {}

	public static IJsStatement<JsBooleanType> of(Boolean value) {
		return JsBoolean.of(value);
	}

	public static IJsStatement<JsStringType> of(String value) {
		return JsString.of(value);
	}

	public static IJsStatement<JsNumberType> of(Number value) {
		return JsNumber.of(value);
	}

	public static <T extends JsAnyType> IJsLiteral<T> ofLiteral(String value) {
		return JsLiteral.<T>of(value);
	}

	public static <T extends JsAnyType> IJsFunction<T> ofFunction(String value) {
		return JsFunction.<T>of(value);
	}

	public static <T extends JsAnyType> JsMapping.Builder<T> mapping() {
		return JsMapping.builder();
	}

	public static <T extends JsAnyType> JsSequence.Builder<T> sequence() {
		return JsSequence.builder();
	}

	public static <T extends JsAnyType> JsFunction.Builder<T> function() {
		return JsFunction.builder();
	}

	public static <T extends JsAnyType> IJsStatement<T> ofLiteral(IModel<String> literalModel) {
		return new LiteralModel<>(literalModel);
	}

	public static IJsStatement<JsElementType> of(Component component) {
		if (!component.getOutputMarkupId()) {
			throw new IllegalStateException(String.format("outputMarkupId must be true to use in javascript statement on %s", component));
		}
		return new ElementComponent(component);
	}

	public static IJsStatement<JsStringType> ofString(IModel<String> valueModel) {
		return new StringModel(valueModel);
	}

	public static IJsStatement<JsNumberType> ofNumber(IModel<? extends Number> valueModel) {
		return new NumberModel(valueModel);
	}

	private static class ElementComponent implements IJsLiteral<JsElementType> {
		private final Component component;
		
		public ElementComponent(Component component) {
			this.component = component;
		}
		
		@Override
		public String value() {
			return "document.getElementById(\"#" + component.getMarkupId() + "\")";
		}
	}

	private static class LiteralModel<T extends JsAnyType> implements IJsLiteral<T>, JsDetachable {
		private final IModel<String> model;
		
		public LiteralModel(IModel<String> literalModel) {
			this.model = literalModel;
		}
		
		@Override
		public String value() {
			return model.getObject();
		}

		@Override
		public Collection<IDetachable> getDetachables() {
			return List.of(model);
		}
	}

	private static class StringModel implements IJsString, JsDetachable {
		private final IModel<String> model;
		
		public StringModel(IModel<String> valueModel) {
			this.model = valueModel;
		}
		
		@Override
		public String value() {
			return model.getObject();
		}

		@Override
		public Collection<IDetachable> getDetachables() {
			return List.of(model);
		}
	}

	private static class NumberModel implements IJsNumber, JsDetachable {
		private final IModel<? extends Number> model;
		
		public NumberModel(IModel<? extends Number> valueModel) {
			this.model = valueModel;
		}
		
		@Override
		public Number value() {
			return model.getObject();
		}

		@Override
		public Collection<IDetachable> getDetachables() {
			return List.of(model);
		}
	}

}
