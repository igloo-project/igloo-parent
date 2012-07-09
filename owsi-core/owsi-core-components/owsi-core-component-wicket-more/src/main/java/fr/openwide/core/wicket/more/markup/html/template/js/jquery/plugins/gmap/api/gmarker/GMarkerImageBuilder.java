package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.gmarker;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.GPoint;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.GSize;

public final class GMarkerImageBuilder implements IGMarkerImageAnchorDefinition, IGMarkerImageGSizeDefinition,
		IGMarkerImageTermination, IGMarkerImageUrlDefinition {

	private GMarkerImage markerImage;
	
	private GMarkerImageBuilder(GMarkerImage markerImage) {
		this.markerImage = markerImage;
	}

	public static IGMarkerImageUrlDefinition build() {
		return new GMarkerImageBuilder(new GMarkerImage());
	}

	@Override
	public IGMarkerImageGSizeDefinition anchor(float x, float y) {
		markerImage.setAnchor(new GPoint(x, y));
		markerImage.setOrigin(new GPoint(0, 0));
		return this;
	}

	@Override
	public IGMarkerImageAnchorDefinition url(String url) {
		markerImage.setUrl(url);
		return this;
	}
	
	@Override
	public IGMarkerImageAnchorDefinition resourceReference(ResourceReference resourceReference) {
		markerImage.setUrl(RequestCycle.get().urlFor(resourceReference, null).toString());
		return this;
	}

	@Override
	public IGMarkerImageTermination scaledSize(int width, int height) {
		markerImage.setScaledSize(new GSize(width, height));
		markerImage.setSize(new GSize(width, height));
		return this;
	}

	@Override
	public GMarkerImage create() {
		return markerImage;
	}

}
