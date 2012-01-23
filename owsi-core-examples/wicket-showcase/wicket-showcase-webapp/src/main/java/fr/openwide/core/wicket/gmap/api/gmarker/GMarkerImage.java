package fr.openwide.core.wicket.gmap.api.gmarker;



import java.io.Serializable;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.gmap.api.GPoint;
import fr.openwide.core.wicket.gmap.api.GSize;

public class GMarkerImage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String url;
	
	private GPoint anchor;
	
	private GPoint origin;
	
	private GSize scaledSize;
	
	private GSize size;
	
	public GMarkerImage(String url, GPoint anchor, GPoint origin, GSize scaledSize, GSize size) {
		if (url == null) {
			throw new IllegalArgumentException("url must be not null ");
		}
		this.url = url;
		this.anchor = anchor;
		this.origin = origin;
		this.scaledSize = scaledSize;
		this.size = size;
	}
	
	public GMarkerImage(ResourceReference resourceReference, GPoint anchor, GPoint origin, GSize scaledSize, GSize size) {
		if (resourceReference == null ) {
			throw new IllegalArgumentException("resourceReference must be not null ");
		}
		this.url = RequestCycle.get().urlFor(resourceReference, null).toString();
		this.anchor = anchor;
		this.origin = origin;
		this.scaledSize = scaledSize;
		this.size = size;
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
