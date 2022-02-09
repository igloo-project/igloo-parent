package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import org.iglooproject.wicket.api.action.IAjaxAction;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;


public interface IActionColumnNoParameterBuildState<T, I> extends IActionColumnCommonBuildState<T, I> {

	IActionColumnNoParameterBuildState<T, I> addAction(BootstrapRenderer<? super T> renderer, IAjaxAction action);

}
