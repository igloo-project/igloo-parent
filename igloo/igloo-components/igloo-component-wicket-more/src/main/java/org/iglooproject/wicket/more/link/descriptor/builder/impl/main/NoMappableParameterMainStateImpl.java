package org.iglooproject.wicket.more.link.descriptor.builder.impl.main;

import igloo.wicket.model.ModelFactories;
import java.util.Collection;
import java.util.List;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.factory.BuilderTargetFactories;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.factory.IBuilderLinkDescriptorFactory;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.LinkParameterTypeInformation;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.INoMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.IOneMappableParameterMainState;
import org.javatuples.Tuple;
import org.springframework.core.convert.TypeDescriptor;

public final class NoMappableParameterMainStateImpl<
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    extends AbstractMainStateImpl<
        INoMappableParameterMainState<
            TLateTargetDefinitionPageLinkDescriptor,
            TLateTargetDefinitionResourceLinkDescriptor,
            TLateTargetDefinitionImageResourceLinkDescriptor>,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    implements INoMappableParameterMainState<
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor> {

  public NoMappableParameterMainStateImpl(
      BuilderTargetFactories<
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          targetFactories) {
    super(targetFactories);
  }

  @Override
  public <TParam1>
      IOneMappableParameterMainState<
              TParam1,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<TParam1> clazz) {
    return new OneMappableParameterMainStateImpl<>(
        this, LinkParameterTypeInformation.valueOf(clazz));
  }

  @Override
  public <TParam1 extends Collection<TElement>, TElement>
      IOneMappableParameterMainState<
              TParam1,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam1> clazz, Class<TElement> elementType) {
    return new OneMappableParameterMainStateImpl<>(
        this, LinkParameterTypeInformation.collection(clazz, elementType));
  }

  @Override
  public <TParam1 extends Collection<?>>
      IOneMappableParameterMainState<
              TParam1,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam1> clazz, TypeDescriptor elementTypeDescriptor) {
    return new OneMappableParameterMainStateImpl<>(
        this, LinkParameterTypeInformation.collection(clazz, elementTypeDescriptor));
  }

  @Override
  public <TParam1 extends Collection<?>>
      IOneMappableParameterMainState<
              TParam1,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(
              Class<? super TParam1> clazz,
              TypeDescriptor elementTypeDescriptor,
              SerializableSupplier2<? extends TParam1> emptyCollectionSupplier) {
    return new OneMappableParameterMainStateImpl<>(
        this,
        LinkParameterTypeInformation.collection(
            clazz, elementTypeDescriptor, emptyCollectionSupplier));
  }

  private <TTarget, TLinkDescriptor> TLinkDescriptor createLinkDescriptor(
      IBuilderLinkDescriptorFactory<TTarget, TLinkDescriptor> linkDescriptorFactory,
      IModel<? extends TTarget> targetModel) {
    return mapperLinkDescriptorFactory(
            linkDescriptorFactory,
            ModelFactories.<IModel<? extends TTarget>, Tuple>constant(targetModel),
            List.of())
        .create(
            new Tuple() {
              private static final long serialVersionUID = 1L;

              @Override
              public int getSize() {
                return 0;
              }
            });
  }

  @Override
  public TLateTargetDefinitionPageLinkDescriptor page(
      IModel<? extends Class<? extends Page>> pageClassModel) {
    return createLinkDescriptor(
        getTargetFactories().getLateTargetDefinitionPageLinkDescriptorFactory(), pageClassModel);
  }

  @Override
  public TLateTargetDefinitionResourceLinkDescriptor resource(
      IModel<? extends ResourceReference> resourceReferenceModel) {
    return createLinkDescriptor(
        getTargetFactories().getLateTargetDefinitionResourceLinkDescriptorFactory(),
        resourceReferenceModel);
  }

  @Override
  public TLateTargetDefinitionImageResourceLinkDescriptor imageResource(
      IModel<? extends ResourceReference> resourceReferenceModel) {
    return createLinkDescriptor(
        getTargetFactories().getLateTargetDefinitionImageResourceLinkDescriptorFactory(),
        resourceReferenceModel);
  }

  @Override
  public TLateTargetDefinitionPageLinkDescriptor page(Class<? extends Page> pageClass) {
    return page(Model.of(pageClass));
  }

  @Override
  public TLateTargetDefinitionResourceLinkDescriptor resource(ResourceReference resourceReference) {
    return resource(Model.of(resourceReference));
  }

  @Override
  public TLateTargetDefinitionImageResourceLinkDescriptor imageResource(
      ResourceReference resourceReference) {
    return imageResource(Model.of(resourceReference));
  }
}
