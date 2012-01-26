package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.gmarker;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

import com.google.code.geocoder.model.LatLng;

import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerAnimation;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerImage;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerShape;
import fr.openwide.core.wicket.gmap.api.utils.GJsStatementUtils;
import fr.openwide.core.wicket.gmap.component.map.GMapPanel;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#MarkerOptions"></>
 */
public class GMarkerOptions  implements ChainableStatement, Serializable {
	private static final long serialVersionUID = 2776022392201208927L;

	private GMapPanel map;
	
	private Boolean autofit;
	
	private String markerId;
	
	private GMarkerAnimation animation;
	
	private LatLng position;
	
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
	
	public GMarkerOptions(String markerId, LatLng position, GMapPanel map) {
		this.markerId = markerId;
		this.position = position;
		this.map = map;
	}
	
	@Override
	public String chainLabel() {
		return "gmap";
	}

	@Override
	public CharSequence[] statementArgs() {
		if (!isValid()) {
			throw new IllegalArgumentException("A marker must be initialized with a markupId, a position and a map");
		}
		
		Options options = new Options();
		options.put("position", GJsStatementUtils.getJavaScriptStatement(position));
		options.put("map", new JsStatement().$(map, "").getStatement().toString() + ".data('gmap').gmap");
		
		if (animation != null) {
			options.put("animation", GJsStatementUtils.getJavaScriptStatement(animation));
		}
		if (shape != null) {
			options.put("shape", GJsStatementUtils.getJavaScriptStatement(shape));
		}
		if (icon != null) {
			options.put("icon", GJsStatementUtils.getJavaScriptStatement(icon));
		}
		if (shadow != null) {
			options.put("shadow", GJsStatementUtils.getJavaScriptStatement(shadow));
		}
		if (title != null) {
			options.put("title", title);
		}
		if (cursor != null) {
			options.put("cursor", JsUtils.quotes(cursor));
		}
		if (raiseOnDrag != null) {
			options.put("raiseOnDrag", raiseOnDrag);
		}
		if (flat != null) {
			options.put("flat", flat);
		}
		if (draggable != null) {
			options.put("draggable", draggable);
		}
		if (clickable != null) {
			options.put("clickable", clickable);
		}
		if (visible != null) {
			options.put("visible", visible);
		}
		if (zIndex != null) {
			options.put("zIndex", zIndex);
		}
		if (autofit == null) {
			autofit = true;
		}
		CharSequence[] args = new CharSequence[4];
		args[0] = JsUtils.quotes("addMarker");
		args[1] = JsUtils.quotes(markerId);
		args[2] = options.getJavaScriptOptions();
		args[3] = autofit.toString();
		return args;
	}
	
	private boolean isValid() {
		if (map != null && position != null && markerId != null) {
			return true;
		} else {
			return false;
		}
	}

	public GMapPanel getMap() {
		return map;
	}

	public void setMap(GMapPanel map) {
		this.map = map;
	}

	public Boolean getAutofit() {
		return autofit;
	}

	public void setAutofit(Boolean autofit) {
		this.autofit = autofit;
	}

	public GMarkerAnimation getAnimation() {
		return animation;
	}

	public void setAnimation(GMarkerAnimation animation) {
		this.animation = animation;
	}

	public LatLng getPosition() {
		return position;
	}

	public void setPosition(LatLng position) {
		this.position = position;
	}

	public Integer getzIndex() {
		return zIndex;
	}

	public void setzIndex(Integer zIndex) {
		this.zIndex = zIndex;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public GMarkerShape getShape() {
		return shape;
	}

	public void setShape(GMarkerShape shape) {
		this.shape = shape;
	}

	public GMarkerImage getIcon() {
		return icon;
	}

	public void setIcon(GMarkerImage icon) {
		this.icon = icon;
	}

	public GMarkerImage getShadow() {
		return shadow;
	}

	public void setShadow(GMarkerImage shadow) {
		this.shadow = shadow;
	}

	public String getCursor() {
		return cursor;
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
	}

	public Boolean getFlat() {
		return flat;
	}

	public void setFlat(Boolean flat) {
		this.flat = flat;
	}

	public Boolean getDraggable() {
		return draggable;
	}

	public void setDraggable(Boolean draggable) {
		this.draggable = draggable;
	}

	public Boolean getClickable() {
		return clickable;
	}

	public void setClickable(Boolean clickable) {
		this.clickable = clickable;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getRaiseOnDrag() {
		return raiseOnDrag;
	}

	public void setRaiseOnDrag(Boolean raiseOnDrag) {
		this.raiseOnDrag = raiseOnDrag;
	}

	public String getMarkerId() {
		return markerId;
	}

	public void setMarkerId(String markerId) {
		this.markerId = markerId;
	}
}
