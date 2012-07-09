package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api;

import java.io.Serializable;

/*
 * see a< href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#Point"></a>
 */
public class GPoint implements Serializable {
	private static final long serialVersionUID = -435118384174872902L;

	// The X coordinate
	private float x;
	
	// The Y coordinate
	private float y;
	
	public GPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
