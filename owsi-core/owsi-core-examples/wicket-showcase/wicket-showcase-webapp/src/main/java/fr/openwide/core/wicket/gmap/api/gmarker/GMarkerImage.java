package fr.openwide.core.wicket.gmap.api.gmarker;

import java.io.Serializable;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.gmap.api.GPoint;
import fr.openwide.core.wicket.gmap.api.GSize;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#MarkerImage"></a>
 */
public class GMarkerImage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String url;
	
	private GPoint anchor;
	
	private GPoint origin;
	
	private GSize scaledSize;
	
	private GSize size;
	
	GMarkerImage() {
	}
	
	public GMarkerImage(GPoint anchor, String url, GSize scaledSize, GPoint origin, GSize size) {
		if (url == null) {
			throw new IllegalArgumentException("url must be not null ");
		}
		this.url = url;
		this.anchor = anchor;
		this.origin = origin;
		this.scaledSize = scaledSize;
		this.size = size;
	}
	
	public GMarkerImage(GPoint anchor, String url, GSize scaledSize) {
		this(anchor, url, scaledSize, null, null);
	}
	
	public GMarkerImage(GPoint anchor, ResourceReference resourceReference, GSize scaledSize) {
		this(anchor, RequestCycle.get().urlFor(resourceReference, null).toString(), scaledSize);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public GPoint getAnchor() {
		return anchor;
	}

	public void setAnchor(GPoint anchor) {
		this.anchor = anchor;
	}

	public GPoint getOrigin() {
		return origin;
	}

	public void setOrigin(GPoint origin) {
		this.origin = origin;
	}

	public GSize getScaledSize() {
		return scaledSize;
	}

	public void setScaledSize(GSize scaledSize) {
		this.scaledSize = scaledSize;
	}

	public GSize getSize() {
		return size;
	}

	public void setSize(GSize size) {
		this.size = size;
	}
}
