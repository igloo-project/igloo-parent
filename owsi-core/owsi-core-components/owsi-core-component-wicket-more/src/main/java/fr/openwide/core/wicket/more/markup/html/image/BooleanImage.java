package fr.openwide.core.wicket.more.markup.html.image;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;

public class BooleanImage extends Image {
	private static final long serialVersionUID = 174241024446763272L;
	
	private static final ResourceReference IMAGE_TRUE = new ResourceReference(AbstractWebPageTemplate.class, "images/icons/tick.png");
	private static final ResourceReference IMAGE_FALSE = new ResourceReference(AbstractWebPageTemplate.class, "images/icons/cross.png");
	
	public BooleanImage(String id, IModel<Boolean> model) {
		super(id, model);
	}
	
	@Override
	public void onComponentTag(final ComponentTag tag) {
		Boolean value = getValue();
		if (value != null && value) {
			setImageResourceReference(IMAGE_TRUE);
		} else {
			setImageResourceReference(IMAGE_FALSE);
		}
		
		super.onComponentTag(tag);
	}
	
	@Override
	public boolean isVisible() {
		return super.isVisible() && (getValue() != null);
	}
	
	private Boolean getValue() {
		return (Boolean) getDefaultModelObject();
	}

}
