package org.iglooproject.wicket.more.link.descriptor.builder.state.terminal;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

/** A state where the build can be terminated, returning the result of the build. */
public interface ILateTargetDefinitionTerminalState<
    TPageResult, TResourceResult, TImageResourceResult> {

  /**
   * @param pageClass The class of the page that the resulting link should point to.
   * @return The resulting link descriptor or link descriptor mapper for this builder, pointing to
   *     the given page.
   */
  TPageResult page(Class<? extends Page> pageClass);

  /**
   * @param resourceReference The reference to the resource that the resulting link should point to.
   * @return The resulting link descriptor or link descriptor mapper for this builder, pointing to
   *     the given resource.
   */
  TResourceResult resource(ResourceReference resourceReference);

  /**
   * @param resourceReference The reference to the resource that the resulting link should point to.
   * @return The resulting link descriptor or link descriptor mapper for this builder, pointing to
   *     the given resource.
   */
  TImageResourceResult imageResource(ResourceReference resourceReference);

  /**
   * @param pageClassModel A model for the class of the page that the resulting link should point
   *     to.
   * @return The resulting link descriptor or link descriptor mapper for this builder, pointing to
   *     whatever page is returned by the given model.
   */
  TPageResult page(IModel<? extends Class<? extends Page>> pageClassModel);

  /**
   * @param resourceReferenceModel A model for the reference to the resource that the resulting link
   *     should point to.
   * @return The resulting link descriptor or link descriptor mapper for this builder, pointing to
   *     whatever resource reference is returned by the given model.
   */
  TResourceResult resource(IModel<? extends ResourceReference> resourceReferenceModel);

  /**
   * @param resourceReferenceModel A model for the reference to the resource that the resulting link
   *     should point to.
   * @return The resulting link descriptor or link descriptor mapper for this builder, pointing to
   *     whatever resource reference is returned by the given model.
   */
  TImageResourceResult imageResource(IModel<? extends ResourceReference> resourceReferenceModel);
}
