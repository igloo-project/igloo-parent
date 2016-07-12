package fr.openwide.core.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;

/**
 * An object that can create a {@link ILinkDescriptor} using one {@link IModel}.
 * <p>Link descriptor mappers are mainly a way to define a link independently from the mapped models.
 * This may come handy, for example when
 * {@link fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IAddedLabelColumnState#withLink(IOneParameterLinkDescriptorMapper)
 * defining columns on a table}: mappers may allow one to make reference to the link descriptor (using a mapper)
 * without a reference to the mapped models (which will only be known when building the table's rows).
 * <p>Note there are also multiple-parameter link descriptor mappers: {@link ITwoParameterLinkDescriptorMapper},
 * {@link IThreeParameterLinkDescriptorMapper}, ...
 * @see IMappableParameterDeclarationState#model(Class)
 */
public interface IOneParameterLinkDescriptorMapper<L, T> extends IDetachable {

	/**
	 * Map the given model to a newly-created {@link ILinkDescriptor} (or {@link ILinkGenerator}, or
	 * {@link ILinkParametersExtractor}, depending on the generic type parameter {@link L}).
	 * <p>The given model will be used in the {@link ILinkDescriptor} as a sink for
	 * {@link ILinkParametersExtractor#extract(org.apache.wicket.request.mapper.parameter.PageParameters) parameter
	 * extraction} and as a parameter source for {@link ILinkGenerator#url() link generation}
	 * @param model The model to be mapped in the resulting {@link ILinkDescriptor}.
	 * @return The resulting {@link ILinkDescriptor}.
	 */
	L map(IModel<T> model);

	<U1 extends T> IOneParameterLinkDescriptorMapper<L, U1> castParameter1();

}
