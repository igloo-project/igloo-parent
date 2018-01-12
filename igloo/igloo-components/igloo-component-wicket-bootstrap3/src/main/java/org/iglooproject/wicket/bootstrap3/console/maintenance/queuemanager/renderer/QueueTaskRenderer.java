package org.iglooproject.wicket.bootstrap3.console.maintenance.queuemanager.renderer;

import java.util.Locale;

import org.iglooproject.jpa.more.infinispan.model.TaskQueueStatus;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRendererInformation;

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

