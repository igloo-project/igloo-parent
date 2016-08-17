package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.factory.IComponentFactory;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;

public interface IConfirmLinkBuilderStepTerminal<L extends AbstractLink, O> extends IComponentFactory<L>, IOneParameterComponentFactory<L, IModel<O>> {

}
