package fr.openwide.core.wicket.more.markup.html.bootstrap.label.model;

import org.apache.wicket.model.IDetachable;
import org.bindgen.Bindable;

@Bindable
public interface IBootstrapColor extends IDetachable {

	public abstract String getCssClassSuffix();

}