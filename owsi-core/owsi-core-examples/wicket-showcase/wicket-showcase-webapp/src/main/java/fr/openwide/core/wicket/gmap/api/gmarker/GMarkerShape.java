package fr.openwide.core.wicket.gmap.api.gmarker;

import java.io.Serializable;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#MarkerShape"></>
 */
public class GMarkerShape implements Serializable {
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

	public GMarkerShapeType getType() {
		return type;
	}

	public void setType(GMarkerShapeType type) {
		this.type = type;
	}

	public Integer[] getCoords() {
		return coords;
	}

	public void setCoords(Integer[] coords) {
		this.coords = coords;
	}
}
