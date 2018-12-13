package org.iglooproject.basicapp.web.application.history.renderer;

import java.util.Locale;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;
import org.iglooproject.jpa.more.business.history.service.IHistoryValueService;
import org.iglooproject.wicket.more.rendering.Renderer;

public final class HistoryValueRenderer extends Renderer<HistoryValue> {

	private static final long serialVersionUID = 4206395563932015847L;

	private boolean initialized = false;

	@SpringBean
	private IHistoryValueService historyValueService;

	private static final HistoryValueRenderer INSTANCE = new HistoryValueRenderer();

	public static HistoryValueRenderer get() {
		return INSTANCE;
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
