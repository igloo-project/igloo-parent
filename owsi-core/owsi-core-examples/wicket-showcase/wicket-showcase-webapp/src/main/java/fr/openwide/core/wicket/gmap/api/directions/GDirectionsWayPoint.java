package fr.openwide.core.wicket.gmap.api.directions;

import java.io.Serializable;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/directions.html"></a>
 */
public class GDirectionsWayPoint implements Serializable {
	private static final long serialVersionUID = -8528964564020155624L;

	private String location; //required
	
	private Boolean stopover; //optional
	
	public GDirectionsWayPoint(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Boolean getStopover() {
		return stopover;
	}

	public void setStopover(Boolean stopover) {
		this.stopover = stopover;
	}
}
