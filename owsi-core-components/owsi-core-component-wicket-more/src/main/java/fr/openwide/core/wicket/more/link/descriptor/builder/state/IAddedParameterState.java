package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;

public interface IAddedParameterState<T extends ILinkDescriptor> {
	
	/**
	 * Makes this parameters mandatory, which means it will be impossible to use the resulting {@link ILinkDescriptor} unless the parameter is present.
	 * <p>This method affects the validation of the resulting {@link ILinkDescriptor#link(String) link}, if any.
	 * @see AbstractDynamicBookmarkableLink
	 */
	IParametersState<T> mandatory();

	/**
	 * Makes this parameters optional, which means it will be possible to use the resulting {@link ILinkDescriptor} even if the parameter is not present.
	 * <p>This method affects the validation of the resulting {@link ILinkDescriptor#link(String) link}, if any.
	 * @see AbstractDynamicBookmarkableLink
	 */
	IParametersState<T> optional();
	
}
