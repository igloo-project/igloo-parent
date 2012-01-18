package fr.openwide.core.wicket.gmap.api.gmarker;



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
	
	@Override
	public String getJavaScriptStatement() {
		return "new google.maps.MarkerImage(" + url.toString() + ", " + size.getJavaScriptStatement() + ", " +
			origin.getJavaScriptStatement() + ", " + anchor.getJavaScriptStatement() + ", " +
			scaledSize.getJavaScriptStatement() + ")";
	}
}
