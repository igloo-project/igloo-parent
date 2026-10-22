package org.iglooproject.jpa.more.rendering;

/**
 * Marker interface for rendering contracts.
 *
 * <p>Renderers can be registered on an interface extending {@link IRenderable} rather than on a
 * concrete type. Lookups performed by {@code IRendererService} and {@code
 * InterfaceAwareConverterLocator} then walk the type hierarchy of the rendered value (interfaces
 * extending {@link IRenderable}, their super-interfaces and superclasses) to find a matching
 * renderer. This allows a single renderer to be shared by several implementations (entities, DTOs,
 * ...).
 */
public interface IRenderable {}
