package org.iglooproject.wicket.more.rendering;

import igloo.bootstrap.common.BootstrapColor;
import java.util.Locale;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation.Builder;

public class TaskStatusRenderer extends BootstrapRenderer<TaskStatus> {

  private static final long serialVersionUID = 4070371618711797908L;

  private static final TaskStatusRenderer INSTANCE = new TaskStatusRenderer();

  public static final TaskStatusRenderer get() {
    return INSTANCE;
  }

  @Override
  protected BootstrapRendererInformation doRender(TaskStatus value, Locale locale) {
    if (value == null) {
      return null;
    }

    Builder builder =
        BootstrapRendererInformation.builder().label(EnumRenderer.get().render(value, locale));

    return switch (value) {
      case TO_RUN -> builder.color(BootstrapColor.SECONDARY).icon("fa fa-fw fa-clock").build();
      case RUNNING -> builder.color(BootstrapColor.PRIMARY).icon("fa fa-fw fa-redo").build();
      case COMPLETED -> builder.color(BootstrapColor.SUCCESS).icon("fa fa-fw fa-check").build();
      case FAILED -> builder.color(BootstrapColor.DANGER).icon("fa fa-fw fa-times").build();
      case INTERRUPTED -> builder.color(BootstrapColor.DANGER).icon("fa fa-fw fa-pause").build();
      case CANCELLED -> builder.color(BootstrapColor.WARNING).icon("fa fa-fw fa-ban").build();
      default -> null;
    };
  }
}
