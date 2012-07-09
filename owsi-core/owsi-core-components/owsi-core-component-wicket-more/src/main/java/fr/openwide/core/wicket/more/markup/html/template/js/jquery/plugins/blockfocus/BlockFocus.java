package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.blockfocus;

import org.odlabs.wiquery.core.options.Options;

public class BlockFocus implements IBlockFocus {

	private static final long serialVersionUID = 6314450447215597924L;

	private Integer delay;

	private Float viewportTopPercentage;

	private Float viewportBottomPercentage;

	@Override
	public String chainLabel() {
		return "blockFocus";
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		if (delay != null) {
			options.put("delay", delay);
		}
		if (viewportBottomPercentage != null) {
			options.put("viewportBottomPercentage", viewportBottomPercentage);
		}
		if (viewportTopPercentage != null) {
			options.put("viewportTopPercentage", viewportTopPercentage);
		}
		return new CharSequence[] { options.getJavaScriptOptions() };
	}

	public static final class Event {
		public static final String FOCUS = "blockFocus";
		public static final String UNFOCUS = "blockUnfocus";
		
		private Event() {
		}
	}

}
