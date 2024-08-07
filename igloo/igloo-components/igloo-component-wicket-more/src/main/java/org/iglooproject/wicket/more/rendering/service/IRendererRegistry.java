package org.iglooproject.wicket.more.rendering.service;

import igloo.wicket.renderer.Renderer;
import org.bindgen.BindingRoot;
import org.iglooproject.commons.util.fieldpath.FieldPath;

public interface IRendererRegistry {

  <R, T> void registerRenderer(
      Class<R> rootType, FieldPath path, Class<T> valueType, Renderer<? super T> renderer);

  <R, T> void registerRenderer(
      Class<R> rootType,
      BindingRoot<R, T> binding,
      Class<T> valueType,
      Renderer<? super T> renderer);

  <T> void registerRenderer(Class<?> rootType, Class<T> valueType, Renderer<? super T> renderer);

  <R, T> void registerRenderer(
      Class<R> rootType, BindingRoot<R, T> binding, Renderer<? super T> renderer);

  <R, T> void registerRenderer(Class<R> rootType, FieldPath path, Renderer<? super T> renderer);

  <T> void registerRenderer(Class<T> valueType, Renderer<? super T> renderer);
}
