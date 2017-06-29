package fr.openwide.core.wicket.more.console.maintenance.queuemanager.renderer;

import java.util.Locale;

import fr.openwide.core.jpa.more.infinispan.model.TaskQueueStatus;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRendererInformation;

public class QueueTaskRenderer {

	private static final IQueueIdStatusRenderer STATUS = new IQueueIdStatusRenderer();

	public static IQueueIdStatusRenderer status() {
		return STATUS;
	}

	private static class IQueueIdStatusRenderer extends BootstrapRenderer<TaskQueueStatus> {
		private static final long serialVersionUID = 1L;

		@Override
		protected BootstrapRendererInformation doRender(TaskQueueStatus value, Locale locale) {
			if (value == null) {
				return BootstrapRendererInformation.builder().build();
			}

			if (value.isActive()) {
				return BootstrapRendererInformation.builder()
						.color(BootstrapColor.SUCCESS)
						.label(getString("business.queuemanager.queueid.status.started", locale))
						.build();
			}

			return BootstrapRendererInformation.builder()
					.color(BootstrapColor.DEFAULT)
					.label(getString("business.queuemanager.queueid.status.stopped", locale))
					.build();
		}

	}

}

