package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.mapping;

import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;

public interface IAddedParameterMappingState<TNextState> {

  /**
   * Makes the last added parameter mapping mandatory, which means it will be impossible to use the
   * resulting {@link ILinkDescriptor} unless the parameter is present.
   */
  TNextState mandatory();

  /**
   * Makes the last added parameter mapping optional, which means it will be possible to use the
   * resulting {@link ILinkDescriptor} even if the parameter is not present.
   */
  TNextState optional();
}
