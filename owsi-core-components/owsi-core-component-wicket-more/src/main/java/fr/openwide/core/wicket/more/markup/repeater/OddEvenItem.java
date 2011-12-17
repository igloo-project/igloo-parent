package fr.openwide.core.wicket.more.markup.repeater;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class OddEvenItem<T> extends Item<T> {
	
	private static final long serialVersionUID = 1669709863079291322L;
	
	private static final String ATTRIBUTE_CLASS = "class";
	private static final String CLASS_ODD = "odd";
	private static final String CLASS_EVEN = "even";
	
	public OddEvenItem(String id, int index) {
		this(id, index, null);
	}
	
	public OddEvenItem(String id, int index, IModel<T> model) {
		super(id, index, model);
		
		add(new AttributeAppender(ATTRIBUTE_CLASS, new Model<String>((index % 2 == 0) ? CLASS_EVEN : CLASS_ODD), " "));
	}
	
	

}
