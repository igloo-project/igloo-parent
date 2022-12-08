package igloo.bootstrap4.markup.html.template.js.bootstrap.collapse;

public enum BootstrapCollapseEventType {

	SHOW("show.bs.collapse"),
	SHOWN("shown.bs.collapse"),
	HIDE("hide.bs.collapse"),
	HIDDEN("hidden.bs.collapse");

	private final String label;

	private BootstrapCollapseEventType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
