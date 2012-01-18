package fr.openwide.core.wicket.gmap.api;

import fr.openwide.core.wicket.gmap.js.util.Constructor;

/*
 * see a< href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#Point"></a>
 */
public class Point implements GValue {
	private static final long serialVersionUID = -435118384174872902L;

	// The X coordinate
	private float x;
	
	// The Y coordinate
	private float y;
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String getJavaScriptStatement() {
		return new Constructor("google.maps.point").add(x).add(y).toJS();
	}
}
