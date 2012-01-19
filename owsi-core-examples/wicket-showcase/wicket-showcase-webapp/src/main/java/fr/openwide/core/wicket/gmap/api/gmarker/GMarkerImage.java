package fr.openwide.core.wicket.gmap.api.gmarker;



import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.ResourceReference;
import org.odlabs.wiquery.core.javascript.JsUtils;

import fr.openwide.core.wicket.gmap.api.GValue;
import fr.openwide.core.wicket.gmap.api.Point;
import fr.openwide.core.wicket.gmap.api.Size;

public class GMarkerImage implements GValue {
	private static final long serialVersionUID = 1L;
	
	private String url;
	
	private Point anchor;
	
	private Point origin;
	
	private Size scaledSize;
	
	private Size size;
	
	public GMarkerImage(String url, Point anchor, Point origin, Size scaledSize, Size size) {
		if (url == null) {
			throw new IllegalArgumentException("url must be not null ");
		}
		this.url = url;
		this.anchor = anchor;
		this.origin = origin;
		this.scaledSize = scaledSize;
		this.size = size;
	}
	
	public GMarkerImage(ResourceReference resourceReference, Point anchor, Point origin, Size scaledSize, Size size) {
		if (resourceReference == null ) {
			throw new IllegalArgumentException("resourceReference must be not null ");
		}
		this.url = RequestCycle.get().urlFor(resourceReference, null).toString();
		this.anchor = anchor;
		this.origin = origin;
		this.scaledSize = scaledSize;
		this.size = size;
	}
	
	@Override
	public String getJavaScriptStatement() {
		StringBuilder statement = new StringBuilder("new google.maps.MarkerImage(");
		statement.append(JsUtils.quotes(url));
		if (size != null) {
			statement.append(",");
			statement.append(size.getJavaScriptStatement());
		}
		if (origin != null) {
			statement.append(",");
			statement.append(origin.getJavaScriptStatement());
		}
		if (anchor != null) {
			statement.append(",");
			statement.append(anchor.getJavaScriptStatement());
		}
		if (scaledSize != null) {
			statement.append(",");
			statement.append(scaledSize.getJavaScriptStatement());
		}
		statement.append(")");
		
		return statement.toString();
	}
}
