package org.iglooproject.wicket.bootstrap4.console.maintenance.task.renderer;

import java.util.Locale;

import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.model.BootstrapColor;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation.Builder;
import org.iglooproject.wicket.more.rendering.EnumRenderer;

public class TaskStatusRenderer extends BootstrapRenderer<TaskStatus> {

	private static final long serialVersionUID = 1L;

	private static final TaskStatusRenderer INSTANCE = new TaskStatusRenderer();

	public static final TaskStatusRenderer get() {
		return INSTANCE;
	}
	
	@Override
	protected BootstrapRendererInformation doRender(TaskStatus value, Locale locale) {
		if (value == null) {
			return null;
		}
		
		Builder builder = BootstrapRendererInformation.builder()
			.label(EnumRenderer.get().render(value, locale));
		
		switch (value) {
		case TO_RUN:
			return builder
				.color(BootstrapColor.SECONDARY)
				.icon("fa fa-fw fa-clock")
				.build();
		case RUNNING:
			return builder
				.color(BootstrapColor.PRIMARY)
				.icon("fa fa-fw fa-redo")
				.build();
		case COMPLETED:
			return builder
				.color(BootstrapColor.SUCCESS)
				.icon("fa fa-fw fa-check-circle")
				.build();
		case FAILED:
			return builder
				.color(BootstrapColor.DANGER)
				.icon("fa fa-fw fa-times-circle")
				.build();
		case INTERRUPTED:
			return builder
				.color(BootstrapColor.DANGER)
				.icon("fa fa-fw fa-pause-circle")
				.build();
		case CANCELLED:
			return builder
				.color(BootstrapColor.WARNING)
				.icon("fa fa-fw fa-ban")
				.build();
		default:
			return null;
		}
	}
}
