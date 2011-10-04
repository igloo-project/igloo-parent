package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.options.Options;

public class Tipsy implements ChainableStatement, Serializable {
	private static final long serialVersionUID = -2090948335676368020L;
	
	private Integer delayIn;
	
	private Integer delayOut;
	
	private String title;
	
	private String fallback;
	
	private TipsyOptionGravity gravity;

	private Boolean html;

	private Boolean fade;
	
	private Boolean live;
	
	private Integer offset;

	private Float opacity;
	
	private TipsyOptionTrigger trigger;
	
	@Override
	public String chainLabel() {
		return "tipsy";
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		if (delayIn != null) {
			options.put("delayIn", delayIn);
		}
		if (delayOut != null) {
			options.put("delayOut", delayOut);
		}
		if (title != null) {
			options.put("title", title);
		}
		if (fallback != null) {
			options.put("fallback", fallback);
		}
		if (gravity != null) {
			options.put("gravity", gravity.getValue());
		}
		if (html != null) {
			options.put("html", html);
		}
		if (fade != null) {
			options.put("fade", fade);
		}
		if (live != null) {
			options.put("live", live);
		}
		if (offset != null) {
			options.put("offset", offset);
		}
		if (opacity != null) {
			options.put("opacity", opacity);
		}
		if (trigger != null) {
			options.put("trigger", trigger.getValue());
		}
		
		CharSequence[] args = new CharSequence[1];
		args[0] = options.getJavaScriptOptions();
		return args;
	}

	public Integer getDelayIn() {
		return delayIn;
	}

	public void setDelayIn(Integer delayIn) {
		this.delayIn = delayIn;
	}

	public Integer getDelayOut() {
		return delayOut;
	}

	public void setDelayOut(Integer delayOut) {
		this.delayOut = delayOut;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFallback() {
		return fallback;
	}

	public void setFallback(String fallback) {
		this.fallback = fallback;
	}

	public TipsyOptionGravity getGravity() {
		return gravity;
	}

	public void setGravity(TipsyOptionGravity gravity) {
		this.gravity = gravity;
	}

	public Boolean getHtml() {
		return html;
	}

	public void setHtml(Boolean html) {
		this.html = html;
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

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Float getOpacity() {
		return opacity;
	}

	public void setOpacity(Float opacity) {
		this.opacity = opacity;
	}

	public TipsyOptionTrigger getTrigger() {
		return trigger;
	}

	public void setTrigger(TipsyOptionTrigger trigger) {
		this.trigger = trigger;
	}
	
}