package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.gmarker;

import org.apache.wicket.request.resource.ResourceReference;

public interface IGMarkerImageUrlDefinition {

	IGMarkerImageAnchorDefinition url(String url);
	
	IGMarkerImageAnchorDefinition resourceReference(ResourceReference reference);
	
}
