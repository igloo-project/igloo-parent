package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.wicket.action.IAjaxAction;


public interface IActionColumnNoParameterBuildState<T, I> extends IActionColumnCommonBuildState<T, I> {

	IActionColumnNoParameterBuildState<T, I> addAction(IBootstrapRenderer<? super T> renderer, IAjaxAction action);

}
