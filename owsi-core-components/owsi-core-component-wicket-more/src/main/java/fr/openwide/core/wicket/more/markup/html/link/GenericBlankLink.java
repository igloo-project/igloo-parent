package fr.openwide.core.wicket.more.markup.html.link;

import org.apache.wicket.IGenericComponent;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

/**
 * A link pointing to nothing in particular. Useful as a basis to add Javascript behaviors.
 */
public abstract class GenericBlankLink<T> extends AbstractLink implements IGenericComponent<T> {

	private static final long serialVersionUID = 7324003053376463554L;

	public GenericBlankLink(String id) {
		super(id);
	}
	
	public GenericBlankLink(String id, IModel<T> model) {
		super(id, model);
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);

		if (isLinkEnabled())
		{
			if (tag.getName().equalsIgnoreCase("a"))
			{
				tag.put("href", "#");
			}
		}
		else
		{
			disableLink(tag);
		}
	}
	
	@Override
	public void setModel(IModel<T> model) {
		super.setDefaultModel(model);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final IModel<T> getModel() {
		return (IModel<T>) getDefaultModel();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final T getModelObject() {
		return (T) getDefaultModelObject();
	}

	@Override
	public void setModelObject(T object) {
		getModel().setObject(object);
	}

}
