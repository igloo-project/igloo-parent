package org.iglooproject.wicket.more.link.descriptor.parameter.mapping;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionException;
import org.iglooproject.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.link.service.ILinkParameterConversionService;

public interface ILinkParameterMappingEntry extends IDetachable {

  /**
   * Inject the mapped model value into {@link PageParameters}, converting the value to a String if
   * necessary.
   *
   * <p>If the mapped model value is null, or if its converted value is null, the parameter will not
   * be added.
   *
   * @param targetParameters The PageParameters the value will be injected into (non-null).
   * @param conversionService The spring {@link ILinkParameterConversionService} to use for
   *     conversion (non-null).
   * @throws LinkParameterInjectionException if a problem occurred during conversion
   */
  void inject(PageParameters targetParameters, ILinkParameterConversionService conversionService)
      throws LinkParameterInjectionException;

  /**
   * Extract the mapped model value from {@link PageParameters}, converting the value if necessary.
   *
   * <p>If the parameter is not found, or if its converted value is null, the model will be set to
   * null.
   *
   * @param targetParameters The PageParameters the value will be extracted from (non-null).
   * @param conversionService The spring {@link ILinkParameterConversionService} to use for
   *     conversion (non-null).
   * @throws LinkParameterExtractionException if a problem occurred during conversion
   */
  void extract(PageParameters sourceParameters, ILinkParameterConversionService conversionService)
      throws LinkParameterExtractionException;

  /**
   * {@link IComponentAssignedModel#wrapOnAssignment(Component) Wraps} the mapped model using this
   * component (if applicable), and returns a copy of this {@link ILinkParameterMappingEntry} with
   * the wrapped model substituted to the original model.
   *
   * <p>If the mapped model does not implement {@link IComponentAssignedModel}, a copy of this
   * {@link ILinkParameterMappingEntry} is returned anyway.
   *
   * @param component The component to wrap
   */
  ILinkParameterMappingEntry wrap(Component component);

  /** Returns the mandatory validator for this paramter mapping entry. */
  ILinkParameterValidator mandatoryValidator();

  @Override
  public abstract void detach();
}
