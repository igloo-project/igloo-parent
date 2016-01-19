package fr.openwide.core.wicket.more.rendering.service;

import org.bindgen.BindingRoot;

import fr.openwide.core.commons.util.fieldpath.FieldPath;
import fr.openwide.core.context.IContextualService;
import fr.openwide.core.wicket.more.rendering.Renderer;


public interface IRendererRegistry extends IContextualService {

	<R, T> void registerRenderer(Class<R> rootType, FieldPath path, Class<T> valueType, Renderer<? super T> renderer);

	<R, T> void registerRenderer(Class<R> rootType, BindingRoot<R, T> binding, Class<T> valueType,
			Renderer<? super T> renderer);

	<T> void registerRenderer(Class<?> rootType, Class<T> valueType, Renderer<? super T> renderer);

	<R, T> void registerRenderer(Class<R> rootType, BindingRoot<R, T> binding, Renderer<? super T> renderer);

	<R, T> void registerRenderer(Class<R> rootType, FieldPath path, Renderer<? super T> renderer);

	<T> void registerRenderer(Class<T> valueType, Renderer<? super T> renderer);
	
}
