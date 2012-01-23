package fr.openwide.core.wicket.gmap.api;

import java.io.Serializable;

/*
 * see a< href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#Size"></a>
 */
public class GSize implements Serializable {
	private static final long serialVersionUID = 6784611603002043120L;

	// The height along the y-axis, in pixels.
	private float height;
	
	// The width along the x-axis, in pixels.
	private float width;
	
	public GSize(float height, float width){
		this.height = height;
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
}
