package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import org.iglooproject.bootstrap.api.renderer.IBootstrapRenderer;
import org.iglooproject.wicket.api.action.IAjaxAction;


public interface IActionColumnNoParameterBuildState<T, I> extends IActionColumnCommonBuildState<T, I> {

	IActionColumnNoParameterBuildState<T, I> addAction(IBootstrapRenderer<? super T> renderer, IAjaxAction action);

}
