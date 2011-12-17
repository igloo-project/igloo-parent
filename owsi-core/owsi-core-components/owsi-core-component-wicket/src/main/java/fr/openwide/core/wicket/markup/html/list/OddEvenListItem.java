package fr.openwide.core.wicket.markup.html.list;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class OddEvenListItem<T> extends ListItem<T> {
	
	private static final long serialVersionUID = 6940017786550102927L;

	private static final String ATTRIBUTE_CLASS = "class";
	private static final String CLASS_ODD = "odd";
	private static final String CLASS_EVEN = "even";

	public OddEvenListItem(int index, IModel<T> model) {
		super(index, model);
		
		add(new AttributeAppender(ATTRIBUTE_CLASS, new Model<String>((index % 2 == 0) ? CLASS_EVEN : CLASS_ODD), " "));
	}
	
}
