package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmarker;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;

import fr.openwide.core.wicket.gmap.api.GLatLng;
import fr.openwide.core.wicket.gmap.api.gmarker.image.GMarkerImage;
import fr.openwide.core.wicket.gmap.api.gmarker.shape.GMarkerShape;

public class GMarkerOptions  implements ChainableStatement, Serializable {
	private static final long serialVersionUID = 2776022392201208927L;

	private GLatLng position;
	
	private Integer zIndex;
	
	private String title;
	
	private GMarkerShape shape;
	
	private GMarkerImage icon;
	
	private GMarkerImage shadow;
	
	private String cursor;
	
	private Boolean flat;
	
	private Boolean draggable;
	
	private Boolean clickable;
	
	private Boolean visible;
	
	private Boolean raiseOnDrag;
	
	@Override
	public String chainLabel() {
		return "gmap";
	}

	@Override
	public CharSequence[] statementArgs() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
