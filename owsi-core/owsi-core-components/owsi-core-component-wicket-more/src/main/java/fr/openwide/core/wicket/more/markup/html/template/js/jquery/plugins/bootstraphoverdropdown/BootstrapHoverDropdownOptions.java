package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstraphoverdropdown;

import org.wicketstuff.wiquery.core.options.Options;

public class BootstrapHoverDropdownOptions extends Options {

	private static final long serialVersionUID = -7025106004471863613L;

	private Integer delay;

	private Boolean instantlyCloseOthers;

	public BootstrapHoverDropdownOptions() {
		super();
	}

	public BootstrapHoverDropdownOptions(Integer delay, Boolean instantlyCloseOthers) {
		super();
		this.delay = delay;
		this.instantlyCloseOthers = instantlyCloseOthers;
	}

	@Override
	public CharSequence getJavaScriptOptions() {
		if (delay != null) {
			put("delay", delay);
		}
		if (instantlyCloseOthers != null) {
			put("instantlyCloseOthers", instantlyCloseOthers);
		}
		
		return super.getJavaScriptOptions();
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Boolean getInstantlyCloseOthers() {
		return instantlyCloseOthers;
	}

	public void setInstantlyCloseOthers(Boolean instantlyCloseOthers) {
		this.instantlyCloseOthers = instantlyCloseOthers;
	}

}
