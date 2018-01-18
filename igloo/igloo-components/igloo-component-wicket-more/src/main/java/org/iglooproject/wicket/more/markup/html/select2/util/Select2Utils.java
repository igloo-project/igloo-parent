package org.iglooproject.wicket.more.markup.html.select2.util;

import org.iglooproject.wicket.more.rendering.Renderer;
import org.wicketstuff.select2.Settings;

public final class Select2Utils {
	
	private static final int DEFAULT_MINIMUM_RESULTS_FOR_SEARCH = 6;
	
	private static final int AJAX_DEFAULT_MINIMUM_INPUT_LENGTH = 2;
	
	public static void setDefaultSettings(Settings settings) {
		settings.setWidth("auto");
//		settings.setNoMatchesKey("common.select2.noChoice");
//		settings.setInputTooShortKey("common.select2.inputTooShort");
		settings.setMinimumResultsForSearch(DEFAULT_MINIMUM_RESULTS_FOR_SEARCH);
		settings.setPlaceholder("common.select2.required.placeholder");
		settings.setAllowClear(true);
		settings.setCloseOnSelect(true);
		settings.setTheme("bootstrap");
	}
	
	public static void setRequiredSettings(Settings settings) {
		if (settings.getPlaceholder() == null) {
			settings.setPlaceholder(Renderer.fromResourceKey("common.select2.required.placeholder"));
		}
		settings.setAllowClear(false);
	}
	
	public static void setDefaultAjaxSettings(Settings settings) {
		setDefaultSettings(settings);
		settings.setMinimumInputLength(AJAX_DEFAULT_MINIMUM_INPUT_LENGTH);
	}
	
	public static void disableSearch(Settings settings) {
		settings.setMinimumResultsForSearch(Integer.MAX_VALUE);
	}
	
	private Select2Utils() {
	}
}
