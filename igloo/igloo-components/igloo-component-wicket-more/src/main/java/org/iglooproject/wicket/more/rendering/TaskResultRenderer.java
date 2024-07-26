package org.iglooproject.wicket.more.rendering;

import igloo.bootstrap.common.BootstrapColor;
import java.util.Locale;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation.Builder;

public class TaskResultRenderer extends BootstrapRenderer<TaskResult> {

  private static final long serialVersionUID = -7575338719059086213L;

  private static final TaskResultRenderer INSTANCE = new TaskResultRenderer();

  public static final TaskResultRenderer get() {
    return INSTANCE;
  }

  @Override
  protected BootstrapRendererInformation doRender(TaskResult value, Locale locale) {
    if (value == null) {
      return null;
    }

    Builder builder =
        BootstrapRendererInformation.builder().label(EnumRenderer.get().render(value, locale));

    switch (value) {
      case SUCCESS:
        return builder.color(BootstrapColor.SUCCESS).icon("fa fa-fw fa-check").build();
      case WARN:
        return builder.color(BootstrapColor.WARNING).icon("fa fa-fw fa-exclamation").build();
      case ERROR:
        return builder.color(BootstrapColor.DANGER).icon("fa fa-fw fa-times").build();
      case FATAL:
        return builder.color(BootstrapColor.DANGER).icon("fa fa-fw fa-times-circle").build();
      default:
        return null;
    }
  }
}
