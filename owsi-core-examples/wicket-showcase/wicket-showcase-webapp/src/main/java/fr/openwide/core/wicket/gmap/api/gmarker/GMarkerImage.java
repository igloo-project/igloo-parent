package fr.openwide.core.wicket.gmap.api.gmarker;

import java.io.Serializable;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.gmap.api.GPoint;
import fr.openwide.core.wicket.gmap.api.GSize;
import fr.openwide.core.wicket.more.lesscss.LessCssResource;

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
	
	public GMarkerImage(GPoint anchor, GPoint origin, GSize scaledSize, GSize size, String url) {
		if (url == null) {
			throw new IllegalArgumentException("url must be not null ");
		}
		this.url = url;
		this.anchor = anchor;
		this.origin = origin;
		this.scaledSize = scaledSize;
		this.size = size;
	}
	
	public GMarkerImage(String icon, GPoint anchor, GPoint origin, GSize scaledSize, GSize size) {
		if (icon == null ) {
			throw new IllegalArgumentException("contextRelativePath must be not null ");
		}
		ResourceReference resourceReference = new GMarkerIconResourceReference(MainTemplate.class, "images/icons/" + icon);
		
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
	
	private static class GMarkerIconResourceReference extends ResourceReference {
		private static final long serialVersionUID = 5367242315017030723L;

		public GMarkerIconResourceReference(Class<?> scope, String name) {
			super(scope, name);
		}
		
		@Override
		public IResource getResource() {
			return new LessCssResource(getScope(), getName(), getLocale(), getStyle(), getVariation());
		}
		
	}
}
