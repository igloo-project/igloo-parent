package fr.openwide.core.wicket.gmap.api.directions;

import java.io.Serializable;

import com.google.code.geocoder.model.LatLng;

public class GDirectionWayPoint implements Serializable {
	private static final long serialVersionUID = -8528964564020155624L;

	private LatLng location; //required
	
	private Boolean stopover; //optional
	
	public GDirectionWayPoint(LatLng location) {
		this.location = location;
	}

	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}

	public Boolean getStopover() {
		return stopover;
	}

	public void setStopover(Boolean stopover) {
		this.stopover = stopover;
	}
}
