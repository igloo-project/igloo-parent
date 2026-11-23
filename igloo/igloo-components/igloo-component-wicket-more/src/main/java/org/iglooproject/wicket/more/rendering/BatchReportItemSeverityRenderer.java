package org.iglooproject.wicket.more.rendering;

import igloo.bootstrap.common.BootstrapColor;
import java.util.Locale;
import org.iglooproject.commons.util.report.BatchReportItemSeverity;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation.Builder;

public class BatchReportItemSeverityRenderer extends BootstrapRenderer<BatchReportItemSeverity> {

  private static final long serialVersionUID = 5328248286898404685L;

  private static final BatchReportItemSeverityRenderer INSTANCE =
      new BatchReportItemSeverityRenderer();

  public static final BatchReportItemSeverityRenderer get() {
    return INSTANCE;
  }

  @Override
  protected BootstrapRendererInformation doRender(BatchReportItemSeverity value, Locale locale) {
    if (value == null) {
      return null;
    }

    Builder builder =
        BootstrapRendererInformation.builder().label(EnumRenderer.get().render(value, locale));

    return switch (value) {
      case TRACE -> builder.color(BootstrapColor.INFO).icon("fa fa-fw fa-code").build();
      case DEBUG -> builder.color(BootstrapColor.INFO).icon("fa fa-fw fa-bug").build();
      case INFO -> builder.color(BootstrapColor.SUCCESS).icon("fa fa-fw fa-chevron-right").build();
      case WARN -> builder.color(BootstrapColor.WARNING).icon("fa fa-fw fa-exclamation").build();
      case ERROR -> builder.color(BootstrapColor.DANGER).icon("fa fa-fw fa-skull").build();
      default -> builder.color(BootstrapColor.SECONDARY).icon("fa fa-fw fa-chevron-right").build();
    };
  }
}
