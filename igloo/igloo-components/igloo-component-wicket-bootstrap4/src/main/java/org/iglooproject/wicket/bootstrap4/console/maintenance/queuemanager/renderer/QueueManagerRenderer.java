package org.iglooproject.wicket.bootstrap4.console.maintenance.queuemanager.renderer;

import java.util.Locale;

import org.iglooproject.jpa.more.infinispan.model.QueueTaskManagerStatus;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRendererInformation;

public class QueueManagerRenderer {

	private static final IQueueManagerStatusRenderer STATUS = new IQueueManagerStatusRenderer();

	public static IQueueManagerStatusRenderer status() {
		return STATUS;
	}

	private static class IQueueManagerStatusRenderer extends BootstrapRenderer<QueueTaskManagerStatus> {
		private static final long serialVersionUID = 1L;
		@Override
		protected BootstrapRendererInformation doRender(QueueTaskManagerStatus value, Locale locale) {
			if (value == null) {
				return BootstrapRendererInformation.builder().build();
			}

			if (value.isQueueManagerActive()) {
				return BootstrapRendererInformation.builder()
						.color(BootstrapColor.SUCCESS)
						.label(getString("business.queuemanager.status.started", locale))
						.build();
			}

			return BootstrapRendererInformation.builder()
					.color(BootstrapColor.DEFAULT)
					.label(getString("business.queuemanager.status.stopped", locale))
					.build();
		}

	}

}

