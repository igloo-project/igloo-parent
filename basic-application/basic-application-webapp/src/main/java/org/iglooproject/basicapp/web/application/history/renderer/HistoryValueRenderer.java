package org.iglooproject.basicapp.web.application.history.renderer;

import java.util.Locale;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;
import org.iglooproject.jpa.more.business.history.service.IHistoryValueService;

import igloo.wicket.renderer.Renderer;

public final class HistoryValueRenderer extends Renderer<HistoryValue> {

	private static final long serialVersionUID = 4206395563932015847L;

	private boolean initialized = false;

	@SpringBean
	private IHistoryValueService historyValueService;

	public static HistoryValueRenderer get() {
		return new HistoryValueRenderer();
	}

	private HistoryValueRenderer() {
	}

	@Override
	public String render(HistoryValue value, Locale locale) {
		if (!initialized) {
			Injector.get().inject(this);
			initialized = true;
		}
		return historyValueService.render(value, locale);
	}

}
