package fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state;


public interface IActionColumnCommonBuildState<T, I> {

	IActionColumnCommonBuildState<T, I> withClassOnElements(String cssClassOnElements);

	I end();

}
