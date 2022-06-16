package igloo.bootstrap.jsmodel;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
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
import igloo.bootstrap.woption.IWOptionComponentFactory;
import igloo.bootstrap.woption.IWOptionDetachable;
import igloo.bootstrap.woption.IWOptionModel;

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

	public static IJsStatement<JsElementType> ofFactory(Function<String, Component> componentFactory) {
		return new ElementComponentFactory(componentFactory);
	}

	public static IJsStatement<JsStringType> ofString(IModel<String> valueModel) {
		return new StringModel(valueModel);
	}

	public static IJsStatement<JsNumberType> ofNumber(IModel<? extends Number> valueModel) {
		return new NumberModel(valueModel);
	}

	private static class ElementComponent implements IJsLiteral<JsElementType> {
		private static final long serialVersionUID = 6326579662056550218L;
		private final Component component;
		
		public ElementComponent(Component component) {
			this.component = component;
		}
		
		@Override
		public String value() {
			return "document.getElementById(\"#" + component.getMarkupId() + "\")";
		}
	}

	private static class ElementComponentFactory implements IJsLiteral<JsElementType>, IWOptionComponentFactory {
		private static final long serialVersionUID = 6326579662056550218L;
		private transient Function<String, Component> componentFactory;
		private Component component;
		
		public ElementComponentFactory(Function<String, Component> componentFactory) {
			this.componentFactory = componentFactory;
		}
		
		@Override
		public String value() {
			if (component == null) {
				throw new IllegalStateException();
			}
			return "document.getElementById(\"#" + component.getMarkupId() + "\")";
		}

		@Override
		public Component createComponent(String wicketId) {
			component = componentFactory.apply(wicketId);
			if (!component.getOutputMarkupId()) {
				throw new IllegalStateException(String.format("Generated component must enable markupId (%s)", component));
			}
			return component;
		}
	}

	private static class LiteralModel<T extends JsAnyType> implements IJsLiteral<T>, IWOptionDetachable, IWOptionModel {
		private static final long serialVersionUID = -6405062352391297878L;
		private IModel<String> model;
		
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

		@Override
		public void wrapModels(Component component) {
			if (model instanceof IComponentAssignedModel) {
				this.model = ((IComponentAssignedModel<String>) model).wrapOnAssignment(component);
			}
		}
	}

	private static class StringModel implements IJsString, IWOptionDetachable, IWOptionModel {
		private static final long serialVersionUID = 7471789246999516516L;
		private IModel<String> model;
		
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

		@Override
		public void wrapModels(Component component) {
			if (model instanceof IComponentAssignedModel) {
				this.model = ((IComponentAssignedModel<String>) model).wrapOnAssignment(component);
			}
		}
	}

	private static class NumberModel implements IJsNumber, IWOptionDetachable, IWOptionModel {
		private static final long serialVersionUID = -3553314976734412526L;
		private IModel<? extends Number> model;
		
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

		@Override
		public void wrapModels(Component component) {
			if (model instanceof IComponentAssignedModel) {
				this.model = ((IComponentAssignedModel<? extends Number>) model).wrapOnAssignment(component);
			}
		}
	}

}
