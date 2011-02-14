package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.options.Options;

public class Tipsy implements ChainableStatement, Serializable {
	private static final long serialVersionUID = -2090948335676368020L;

	private String title;
	
	private Boolean fade;
	
	private Boolean live;

	@Override
	public String chainLabel() {
		return "tipsy";
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		if (fade != null) {
			options.put("fade", fade);
		}
		if (title != null) {
			options.put("title", title);
		}
		if (live != null) {
			options.put("live", live);
		}
		CharSequence[] args = new CharSequence[1];
		args[0] = options.getJavaScriptOptions();
		return args;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getFade() {
		return fade;
	}

	public void setFade(Boolean fade) {
		this.fade = fade;
	}

	public Boolean getLive() {
		return live;
	}

	public void setLive(Boolean live) {
		this.live = live;
	}

}
