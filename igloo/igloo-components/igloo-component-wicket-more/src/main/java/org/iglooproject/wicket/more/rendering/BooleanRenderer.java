package org.iglooproject.wicket.more.rendering;

import igloo.bootstrap.common.BootstrapColor;
import igloo.bootstrap.common.IBootstrapColor;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation;

public class BooleanRenderer extends BootstrapRenderer<Boolean> {

  private static final long serialVersionUID = -6934415690685574154L;

  private static final BooleanRenderer TRUE_FALSE = BooleanRenderer.builder().trueFalse().build();

  private static final BooleanRenderer YES_NO = BooleanRenderer.builder().yesNo().build();

  public static BooleanRenderer get() {
    return yesNo();
  }

  public static BooleanRenderer yesNo() {
    return YES_NO;
  }

  public static BooleanRenderer trueFalse() {
    return TRUE_FALSE;
  }

  private final String resourceKey;

  private final ValueInformation onTrue;

  private final ValueInformation onFalse;

  private BooleanRenderer(Builder builder) {
    super();
    this.resourceKey = Objects.requireNonNull(builder.resourceKey);
    this.onTrue = Objects.requireNonNull(builder.onTrueBuilder.information);
    this.onFalse = Objects.requireNonNull(builder.onFalseBuilder.information);
  }

  @Override
  protected BootstrapRendererInformation doRender(Boolean value, Locale locale) {
    if (value == null) {
      return null;
    }
    if (value) {
      return BootstrapRendererInformation.builder()
          .label(renderLabel(value, locale))
          .icon(onTrue.icon)
          .color(onTrue.color)
          .build();
    } else {
      return BootstrapRendererInformation.builder()
          .label(renderLabel(value, locale))
          .icon(onFalse.icon)
          .color(onFalse.color)
          .build();
    }
  }

  private String renderLabel(Boolean value, Locale locale) {
    return new StringResourceModel(resourceKey, Model.of(value)).getObject();
  }

  private static final class ValueInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private IBootstrapColor color;

    private String icon;

    public ValueInformation color(IBootstrapColor color) {
      this.color = Objects.requireNonNull(color);
      return this;
    }

    public ValueInformation icon(String icon) {
      this.icon = Objects.requireNonNull(icon);
      return this;
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private String resourceKey;

    private ValueBuilder onTrueBuilder = new ValueBuilder(this);

    private ValueBuilder onFalseBuilder = new ValueBuilder(this);

    private Builder() {
      yesNo();
      onTrueBuilder.information().color(BootstrapColor.SUCCESS).icon("fa fa-fw fa-check");
      onFalseBuilder.information().color(BootstrapColor.DANGER).icon("fa fa-fw fa-times");
    }

    public Builder resourceKey(String resourceKey) {
      this.resourceKey = resourceKey;
      return this;
    }

    public Builder yesNo() {
      return resourceKey("common.boolean.yesNo.${}");
    }

    public Builder trueFalse() {
      return resourceKey("common.boolean.${}");
    }

    public ValueBuilder onTrue() {
      return onTrueBuilder;
    }

    public ValueBuilder onFalse() {
      return onFalseBuilder;
    }

    public BooleanRenderer build() {
      return new BooleanRenderer(this);
    }
  }

  public static final class ValueBuilder {

    private final Builder builder;

    private ValueInformation information = new ValueInformation();

    private ValueBuilder(Builder builder) {
      this.builder = Objects.requireNonNull(builder);
    }

    public ValueInformation information() {
      return information;
    }

    public Builder done() {
      return builder;
    }
  }
}
