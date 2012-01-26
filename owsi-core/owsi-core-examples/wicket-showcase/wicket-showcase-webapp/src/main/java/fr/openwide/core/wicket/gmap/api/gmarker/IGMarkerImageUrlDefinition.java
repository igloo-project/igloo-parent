package fr.openwide.core.wicket.gmap.api.gmarker;

import org.apache.wicket.request.resource.ResourceReference;

public interface IGMarkerImageUrlDefinition {

	IGMarkerImageAnchorDefinition url(String url);
	
	IGMarkerImageAnchorDefinition resourceReference(ResourceReference reference);
	
}
