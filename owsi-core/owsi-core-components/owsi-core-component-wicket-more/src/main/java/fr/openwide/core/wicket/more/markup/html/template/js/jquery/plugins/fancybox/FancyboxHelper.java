package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

public final class FancyboxHelper {
	
	public static String getResize() {
		return "$.fancybox.resize()";
	}
	
	public static String getClose() {
		return "$.fancybox.close()";
	}
	
	public static String getTriggerFancyboxCleanup() {
		return "$.fancybox.trigger('fancybox-cleanup')";
	}
	
	private FancyboxHelper() {
	}

}
