package igloo.bootstrap.jsmodel;

import igloo.bootstrap.js.statement.IJsFunction;
import igloo.bootstrap.js.statement.IJsLiteral;
import igloo.bootstrap.js.statement.IJsNumber;
import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.js.statement.IJsString;
import igloo.bootstrap.js.statement.IJsVariable;
import igloo.bootstrap.js.statement.JsBoolean;
import igloo.bootstrap.js.statement.JsFunction;
import igloo.bootstrap.js.statement.JsLiteral;
import igloo.bootstrap.js.statement.JsLocalDateModel;
import igloo.bootstrap.js.statement.JsLocalDatePairModel;
import igloo.bootstrap.js.statement.JsLocalDateTimeModel;
import igloo.bootstrap.js.statement.JsLocalDateTimePairModel;
import igloo.bootstrap.js.statement.JsLocalTimeModel;
import igloo.bootstrap.js.statement.JsMapping;
import igloo.bootstrap.js.statement.JsNumber;
import igloo.bootstrap.js.statement.JsSequence;
import igloo.bootstrap.js.statement.JsString;
import igloo.bootstrap.js.statement.JsVariable;
import igloo.bootstrap.js.statement.JsYearModel;
import igloo.bootstrap.js.statement.JsYearMonthModel;
import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.woption.IWOptionDetachable;
import igloo.bootstrap.woption.IWOptionModel;
import igloo.wicket.factory.IComponentFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.javatuples.Pair;

public class JsHelpers {

  private JsHelpers() {}

  public static IJsStatement of(Boolean value) {
    return JsBoolean.of(value);
  }

  public static IJsStatement of(String value) {
    return JsString.of(value);
  }

  public static IJsStatement of(Number value) {
    return JsNumber.of(value);
  }

  public static IJsStatement ofLocalDateModel(IModel<LocalDate> localDate) {
    return JsLocalDateModel.of(localDate);
  }

  public static IJsStatement ofYearMonthModel(IModel<YearMonth> yearMonthModel) {
    return JsYearMonthModel.of(yearMonthModel);
  }

  public static IJsStatement ofYearModel(IModel<Year> yearModel) {
    return JsYearModel.of(yearModel);
  }

  public static IJsStatement ofLocalDateTimeModel(IModel<LocalDateTime> localDateTime) {
    return JsLocalDateTimeModel.of(localDateTime);
  }

  public static IJsStatement ofLocalTimeModel(IModel<LocalTime> localTimeModel) {
    return JsLocalTimeModel.of(localTimeModel);
  }

  public static IJsStatement ofLocalDatePair(IModel<Pair<LocalDate, LocalDate>> value) {
    return JsLocalDatePairModel.of(value);
  }

  public static IJsStatement ofLocalDateTimePair(IModel<Pair<LocalDateTime, LocalDateTime>> value) {
    return JsLocalDateTimePairModel.of(value);
  }

  public static IJsLiteral ofLiteral(String value) {
    return JsLiteral.of(value);
  }

  /** Create a variable type to distinct a string value from a variable declaration */
  public static IJsVariable ofJsVariable(String value) {
    return JsVariable.of(value);
  }

  public static IJsFunction ofFunction(String value) {
    return JsFunction.of(value);
  }

  public static JsMapping.Builder mapping() {
    return JsMapping.builder();
  }

  public static JsSequence.Builder sequence() {
    return JsSequence.builder();
  }

  public static JsFunction.Builder function() {
    return JsFunction.builder();
  }

  public static IJsStatement ofLiteral(IModel<String> literalModel) {
    return new LiteralModel<>(literalModel);
  }

  public static IJsStatement of(Component component) {
    return new ElementComponent(component);
  }

  public static IJsStatement ofFactory(Function<String, Component> componentFactory) {
    return new ElementComponentFactory(componentFactory);
  }

  public static IJsStatement ofString(IModel<String> valueModel) {
    return new StringModel(valueModel);
  }

  public static IJsStatement ofNumber(IModel<? extends Number> valueModel) {
    return new NumberModel(valueModel);
  }

  private static class ElementComponent implements IJsLiteral {
    private static final long serialVersionUID = 6326579662056550218L;
    private final Component component;

    public ElementComponent(Component component) {
      this.component = component;
    }

    @Override
    public String value() {
      return "document.getElementById(\"" + component.getMarkupId() + "\")";
    }
  }

  private static class ElementComponentFactory implements IJsLiteral, IComponentFactory<Component> {
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
      return "document.getElementById(\"" + component.getMarkupId() + "\")";
    }

    @Override
    public Component create(String wicketId) {
      component = componentFactory.apply(wicketId);
      return component;
    }
  }

  private static class LiteralModel<T extends JsAnyType>
      implements IJsLiteral, IWOptionDetachable, IWOptionModel {
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
        this.model =
            ((IComponentAssignedModel<? extends Number>) model).wrapOnAssignment(component);
      }
    }
  }
}
