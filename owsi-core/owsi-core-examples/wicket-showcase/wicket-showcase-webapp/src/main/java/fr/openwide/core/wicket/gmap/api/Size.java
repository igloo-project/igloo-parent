package fr.openwide.core.wicket.gmap.api;

import fr.openwide.core.wicket.gmap.js.util.Constructor;

/*
 * see a< href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#Size"></a>
 */
public class Size implements GValue {
	private static final long serialVersionUID = 6784611603002043120L;

	// The height along the y-axis, in pixels.
	private float height;
	
	// The width along the x-axis, in pixels.
	private float width;
	
	public Size(float height, float width){
		this.height = height;
		this.width = width;
	}
	
	@Override
	public String getJavaScriptStatement() {
		return new Constructor("google.maps.Size").add(width).add(height).toJS();
	}
}
