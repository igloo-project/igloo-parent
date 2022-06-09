package igloo.wicket.component;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Classes;

public class EnumCoreLabel<E extends Enum<E>> extends AbstractGenericLabel<E, EnumCoreLabel<E>> {

	private static final long serialVersionUID = 1809672380316539749L;
	
	public EnumCoreLabel(String id, IModel<E> model) {
		super(id, model);
	}
	
	@Override
	protected EnumCoreLabel<E> thisAsT() {
		return this;
	}
	
	@Override
	public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
		E value = getModelObject();
		if (value != null) {
			replaceComponentTagBody(markupStream, openTag, getString(resourceKey(value)));
		} else {
			super.onComponentTagBody(markupStream, openTag);
		}
	}
	
	/**
	 * Converts enum value into a resource key that should be used to lookup the text the label will
	 * display
	 * 
	 * @param value
	 * @return resource key
	 */
	protected String resourceKey(E value) {
		return Classes.simpleName(value.getDeclaringClass()) + '.' + value.name();
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		// always transform the tag to <span></span> so even labels defined as <span/> render
		tag.setType(TagType.OPEN);
	}
}
