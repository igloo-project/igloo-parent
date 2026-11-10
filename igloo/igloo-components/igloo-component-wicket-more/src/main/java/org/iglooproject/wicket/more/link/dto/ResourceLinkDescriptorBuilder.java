package org.iglooproject.wicket.more.link.dto;

import java.util.Objects;
import java.util.function.Consumer;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.dto.base.IResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.dto.dto.ILinkDataDto;
import org.iglooproject.wicket.more.link.dto.mapping.LinkDataDtoMappingRegistry;

/** Resource counterpart of {@link PageLinkDescriptorBuilder}. */
public class ResourceLinkDescriptorBuilder<D extends ILinkDataDto> {

  private final ResourceReference resourceReference;
  private final SerializableSupplier2<D> dataDtoFactory;
  private final LinkDataDtoMappingRegistry<D> mappingRegistry = new LinkDataDtoMappingRegistry<>();
  private SerializableFunction2<D, String> permissionProvider = _ -> null;

  private ResourceLinkDescriptorBuilder(
      ResourceReference resourceReference, SerializableSupplier2<D> dataDtoFactory) {
    this.resourceReference = Objects.requireNonNull(resourceReference);
    this.dataDtoFactory = Objects.requireNonNull(dataDtoFactory);
  }

  public static <D extends ILinkDataDto> ResourceLinkDescriptorBuilder<D> create(
      ResourceReference resourceReference, SerializableSupplier2<D> dataDtoFactory) {
    return new ResourceLinkDescriptorBuilder<>(resourceReference, dataDtoFactory);
  }

  public ResourceLinkDescriptorBuilder<D> mapping(
      Consumer<LinkDataDtoMappingRegistry<D>> configurer) {
    configurer.accept(mappingRegistry);
    return this;
  }

  public ResourceLinkDescriptorBuilder<D> permission(
      SerializableFunction2<D, String> permissionProvider) {
    this.permissionProvider = Objects.requireNonNull(permissionProvider);
    return this;
  }

  public ResourceLinkDescriptorBuilder<D> permission(String permission) {
    return permission(_ -> permission);
  }

  public IResourceLinkDescriptor<D> build() {
    return new ResourceLinkDescriptorImpl<>(
        resourceReference, dataDtoFactory, mappingRegistry.build(), permissionProvider);
  }
}
