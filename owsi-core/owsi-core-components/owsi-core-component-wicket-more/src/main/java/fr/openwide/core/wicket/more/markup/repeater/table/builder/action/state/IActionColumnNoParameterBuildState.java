package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;

import fr.openwide.core.wicket.more.markup.html.action.IAjaxAction;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;


public interface IActionColumnNoParameterBuildState<T, I> extends IActionColumnCommonBuildState<T, I> {

	IActionColumnNoParameterBuildState<T, I> addAction(BootstrapRenderer<? super T> renderer, IAjaxAction action);

}
