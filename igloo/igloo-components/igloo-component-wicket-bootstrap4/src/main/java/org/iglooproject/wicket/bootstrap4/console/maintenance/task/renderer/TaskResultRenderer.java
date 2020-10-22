package org.iglooproject.wicket.bootstrap4.console.maintenance.task.renderer;

import java.util.Locale;

import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.model.BootstrapColor;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation.Builder;
import org.iglooproject.wicket.more.rendering.EnumRenderer;

public class TaskResultRenderer extends BootstrapRenderer<TaskResult> {

	private static final long serialVersionUID = 1L;

	private static final TaskResultRenderer INSTANCE = new TaskResultRenderer();

	public static final TaskResultRenderer get() {
		return INSTANCE;
	}
	
	@Override
	protected BootstrapRendererInformation doRender(TaskResult value, Locale locale) {
		if (value == null) {
			return null;
		}
		
		Builder builder = BootstrapRendererInformation.builder()
			.label(EnumRenderer.get().render(value, locale));
		
		switch (value) {
		case SUCCESS:
			return builder
				.color(BootstrapColor.SUCCESS)
				.icon("fa fa-fw fa-check-circle")
				.build();
		case WARN:
			return builder
				.color(BootstrapColor.WARNING)
				.icon("fa fa-fw fa-exclamation-triangle")
				.build();
		case ERROR:
			return builder
				.color(BootstrapColor.DANGER)
				.icon("fa fa-fw fa-exclamation-circle")
				.build();
		case FATAL:
			return builder
				.color(BootstrapColor.DANGER)
				.icon("fa fa-fw fa-times-circle")
				.build();
		default:
			return null;
		}
	}
}
