package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.scrollinviewport;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;

public class ScrollInViewport implements ChainableStatement, Serializable {
	
	private static final long serialVersionUID = 1428052564237850262L;
	
	private static final String SCROLL_IN_VIEWPORT = "scrollInViewport";
	
	private Integer delay;
	
	private Integer margin;
	
	private Component referenceComponent;
	
	public ScrollInViewport() {
		super();
	}
	
	@Override
	public String chainLabel() {
		return SCROLL_IN_VIEWPORT;
	}
	
	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		
		if (delay != null) {
			options.put("delay", delay);
		}
		
		if (margin != null) {
			options.put("margin", margin);
		}
		
		if (referenceComponent != null) {
			options.put("referenceComponent", new JsStatement().$(referenceComponent).render(false).toString());
		}
		
		return new CharSequence[] { options.getJavaScriptOptions() };
	}
	
	public Integer getDelay() {
		return delay;
	}
	
	/**
	 * Délai de calcul de la position en millisecondes.<br />
	 * <strong>Attention</strong> : en dessous de 50ms, des ralentissements sur
	 * l'affichage d'un panneau simple peuvent apparaître.
	 * 
	 * @param delay en ms
	 */
	public void setDelay(Integer delay) {
		this.delay = delay;
	}
	
	public Integer getMargin() {
		return margin;
	}
	
	public void setMargin(Integer margin) {
		this.margin = margin;
	}
	
	public Component getReferenceComponent() {
		return referenceComponent;
	}
	
	public void setReferenceComponent(Component referenceComponent) {
		this.referenceComponent = referenceComponent;
	}
}
