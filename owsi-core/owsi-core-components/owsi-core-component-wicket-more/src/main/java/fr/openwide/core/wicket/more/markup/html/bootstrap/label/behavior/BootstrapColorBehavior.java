package fr.openwide.core.wicket.more.markup.html.bootstrap.label.behavior;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.IBootstrapColor;

public class BootstrapColorBehavior extends ClassAttributeAppender {

	private static final long serialVersionUID = 7272137227196691195L;
	
	public static BootstrapColorBehavior label(IModel<IBootstrapColor> colorModel) {
		return new BootstrapColorBehavior("label label-", colorModel);
	}
	
	public static BootstrapColorBehavior alert(IModel<IBootstrapColor> colorModel) {
		return new BootstrapColorBehavior("alert alert-", colorModel);
	}
	
	public static BootstrapColorBehavior bg(IModel<IBootstrapColor> colorModel) {
		return new BootstrapColorBehavior("bg-", colorModel);
	}
	
	public static BootstrapColorBehavior btn(IModel<IBootstrapColor> colorModel) {
		return new BootstrapColorBehavior("btn-", colorModel);
	}

	public BootstrapColorBehavior(final String cssClassPrefix, final IModel<IBootstrapColor> colorModel) {
		super(new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				IBootstrapColor color = colorModel.getObject();
				if (color == null) {
					return null;
				} else {
					return cssClassPrefix + color.getCssClassSuffix();
				}
			}
			
			@Override
			public void detach() {
				super.detach();
				colorModel.detach();
			}
		});
	}

}
