package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.infowindow;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

import com.google.code.geocoder.model.LatLng;

import fr.openwide.core.wicket.gmap.api.GSize;
import fr.openwide.core.wicket.gmap.api.utils.GJsStatementUtils;
import fr.openwide.core.wicket.gmap.component.gmap.GMapPanel;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#InfoWindowOptions"></a>
 * <a href="http://google-maps-utility-library-v3.googlecode.com/svn/trunk/infobubble/"></a>
 */

public class GInfoBubbleOptions implements ChainableStatement, Serializable {
	private static final long serialVersionUID = -2530841040713652424L;

	private GMapPanel map;
	private String markerId;
	private String event;
	
	// InfoWindow
	private LatLng position;
	private String content;
	private Float zIndex;
	private Integer maxWidth;
	private GSize pixelOffset;
	private Boolean disableAutoPan;
	
	// InfoBubble
	private Boolean hideCloseButton;
	private String backgroundColor;
	private String borderColor;
	private Integer borderRadius;
	private Integer borderWidth;
	private Integer padding;
	private Integer arrowPosition;
	private Boolean disableAnimation;
	private Integer minWidth;
	private Integer shadowStyle;
	private Integer arrowSize;
	private String arrowStyle;
	private String backgroundClassName;
	
	
	
	public GInfoBubbleOptions(GMapPanel map, String markerId, String event, String content) {
		this.map = map;
		this.markerId = markerId;
		this.event = event;
		this.content = content;
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
		options.put("content", JsUtils.quotes(content));
		if (position != null) {
			options.put("position", GJsStatementUtils.getJavaScriptStatement(position));
		}
		if (maxWidth != null) {
			options.put("maxWidth", maxWidth);
		}
		if (pixelOffset != null) {
			options.put("pixelOffset", GJsStatementUtils.getJavaScriptStatement(pixelOffset));
		}
		if (zIndex != null) {
			options.put("zIndex", zIndex);
		}
		if (disableAutoPan != null) {
			options.put("disableAutoPan", disableAutoPan);
		}
		if (hideCloseButton != null) {
			options.put("hideCloseButton", hideCloseButton);
		}
		if (hideCloseButton != null) {
			options.put("hideCloseButton", hideCloseButton);
		}
		if (backgroundColor != null) {
			options.put("backgroundColor", JsUtils.quotes(backgroundColor));
		}
		if (borderColor != null) {
			options.put("borderColor", JsUtils.quotes(borderColor));
		}
		if (borderRadius != null) {
			options.put("borderRadius", borderRadius);
		}
		if (borderWidth != null) {
			options.put("borderWidth", borderWidth);
		}
		if (padding != null) {
			options.put("padding", padding);
		}
		if (arrowPosition != null) {
			options.put("arrowPosition", arrowPosition);
		}
		if (disableAnimation != null) {
			options.put("disableAnimation", disableAnimation);
		}
		if (minWidth != null) {
			options.put("minWidth", minWidth);
		}
		if (shadowStyle != null) {
			options.put("shadowStyle", shadowStyle);
		}
		if (arrowSize != null) {
			options.put("arrowSize", arrowSize);
		}
		if (arrowStyle != null) {
			options.put("arrowStyle", JsUtils.quotes(arrowStyle));
		}
		if (backgroundClassName != null) {
			options.put("backgroundClassName", JsUtils.quotes(backgroundClassName));
		}
	
		CharSequence[] args = new CharSequence[4];
		args[0] = JsUtils.quotes("addInfoBubble");
		args[1] = JsUtils.quotes(markerId);
		args[2] = JsUtils.quotes(event);
		args[3] = options.getJavaScriptOptions();
		return args;
	}
	
	private boolean isValid(){
		if (map != null && markerId != null && event != null && content != null) {
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

	public String getMarkerId() {
		return markerId;
	}

	public void setMarkerId(String markerId) {
		this.markerId = markerId;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public LatLng getPosition() {
		return position;
	}

	public void setPosition(LatLng position) {
		this.position = position;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Float getzIndex() {
		return zIndex;
	}

	public void setzIndex(Float zIndex) {
		this.zIndex = zIndex;
	}

	public Integer getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(Integer maxWidth) {
		this.maxWidth = maxWidth;
	}

	public GSize getPixelOffset() {
		return pixelOffset;
	}

	public void setPixelOffset(GSize pixelOffset) {
		this.pixelOffset = pixelOffset;
	}

	public Boolean getDisableAutoPan() {
		return disableAutoPan;
	}

	public void setDisableAutoPan(Boolean disableAutoPan) {
		this.disableAutoPan = disableAutoPan;
	}

	public Boolean getHideCloseButton() {
		return hideCloseButton;
	}

	public void setHideCloseButton(Boolean hideCloseButton) {
		this.hideCloseButton = hideCloseButton;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public Integer getBorderRadius() {
		return borderRadius;
	}

	public void setBorderRadius(Integer borderRadius) {
		this.borderRadius = borderRadius;
	}

	public Integer getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
	}

	public Integer getPadding() {
		return padding;
	}

	public void setPadding(Integer padding) {
		this.padding = padding;
	}

	public Integer getArrowPosition() {
		return arrowPosition;
	}

	public void setArrowPosition(Integer arrowPosition) {
		this.arrowPosition = arrowPosition;
	}

	public Boolean getDisableAnimation() {
		return disableAnimation;
	}

	public void setDisableAnimation(Boolean disableAnimation) {
		this.disableAnimation = disableAnimation;
	}

	public Integer getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(Integer minWidth) {
		this.minWidth = minWidth;
	}

	public Integer getShadowStyle() {
		return shadowStyle;
	}

	public void setShadowStyle(Integer shadowStyle) {
		this.shadowStyle = shadowStyle;
	}

	public Integer getArrowSize() {
		return arrowSize;
	}

	public void setArrowSize(Integer arrowSize) {
		this.arrowSize = arrowSize;
	}

	public String getArrowStyle() {
		return arrowStyle;
	}

	public void setArrowStyle(String arrowStyle) {
		this.arrowStyle = arrowStyle;
	}

	public String getBackgroundClassName() {
		return backgroundClassName;
	}

	public void setBackgroundClassName(String backgroundClassName) {
		this.backgroundClassName = backgroundClassName;
	}
}
