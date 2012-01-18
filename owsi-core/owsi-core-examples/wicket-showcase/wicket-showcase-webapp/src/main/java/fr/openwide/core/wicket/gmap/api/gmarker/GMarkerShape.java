package fr.openwide.core.wicket.gmap.api.gmarker;

import fr.openwide.core.wicket.gmap.api.GValue;

public class GMarkerShape implements GValue {
	private static final long serialVersionUID = -5846521106702393028L;
	
	private GMarkerShapeType type;
	
	private Integer[] coords;
	
	public GMarkerShape(GMarkerShapeType type, Integer[] coords) {
		this.type = type;
		this.coords = coords;
		
		switch(this.type) {
			case CIRCLE:
				if(coords.length != 3) {
					throw new IllegalArgumentException("For type " + type.getValue() + ", coords must be like [x, y, r]" );
				}
				break;
			case POLY:
				if(coords.length < 4) {
					throw new IllegalArgumentException("For type " + type.getValue() + ", coords must be like [x1, y1, x2, y2, ...]" );
				}
				break;
			case RECTANGLE:
				if(coords.length != 4) {
					throw new IllegalArgumentException("For type " + type.getValue() + ", coords must be like [x1, y1, x2, y2]" );
				}
				break;
		}
	}
	
	@Override
	public String getJavaScriptStatement() {
		return "new google.maps.MarkerShape{type:" + type.getValue() + ", coords:" + getCoordsJSStatement() + "}";
	}
	
	private String getCoordsJSStatement() {
		StringBuffer array = new StringBuffer();
		array.append("[");
	
		if (coords.length > 0)
		{
			array.append(coords[0]);
	
			for (int i = 1; i < coords.length; i++)
			{
				array.append(", ").append(coords[i]);
			}
		}
		array.append("]");
		return array.toString();
	}
}
