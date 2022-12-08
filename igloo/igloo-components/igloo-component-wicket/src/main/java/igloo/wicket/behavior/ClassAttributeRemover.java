package igloo.wicket.behavior;

import org.apache.wicket.model.IModel;

public class ClassAttributeRemover extends AttributeRemover {

	private static final long serialVersionUID = 5508635135209417699L;

	public ClassAttributeRemover(IModel<?> removeModel) {
		super("class", removeModel);
	}

}
