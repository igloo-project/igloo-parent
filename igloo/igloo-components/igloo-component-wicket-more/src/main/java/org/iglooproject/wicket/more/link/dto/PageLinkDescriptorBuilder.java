package org.iglooproject.wicket.more.link.dto;

import java.util.Objects;
import java.util.function.Consumer;
import org.apache.wicket.Page;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.dto.base.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.dto.dto.ILinkDataDto;
import org.iglooproject.wicket.more.link.dto.mapping.LinkDataDtoMappingRegistry;

public class PageLinkDescriptorBuilder<P extends Page, D extends ILinkDataDto> {

  private final Class<P> pageClass;
  private final SerializableSupplier2<D> dataDtoFactory;
  private final LinkDataDtoMappingRegistry<D> mappingRegistry = new LinkDataDtoMappingRegistry<>();
  private SerializableFunction2<D, String> permissionProvider = _ -> null;

  private PageLinkDescriptorBuilder(Class<P> pageClass, SerializableSupplier2<D> dataDtoFactory) {
    this.pageClass = pageClass;
    this.dataDtoFactory = dataDtoFactory;
  }

  // TODO RFO est -qu'on fait un constructeur sans DTO ?
  public static <P extends Page, D extends ILinkDataDto> PageLinkDescriptorBuilder<P, D> create(
      Class<P> pageClass, SerializableSupplier2<D> dtoFactory) {
    return new PageLinkDescriptorBuilder<>(pageClass, dtoFactory);
  }

  public PageLinkDescriptorBuilder<P, D> mapping(
      Consumer<LinkDataDtoMappingRegistry<D>> configurer) {
    configurer.accept(mappingRegistry);
    return this;
  }

  public PageLinkDescriptorBuilder<P, D> permission(
      SerializableFunction2<D, String> permissionProvider) {
    this.permissionProvider = Objects.requireNonNull(permissionProvider);
    return this;
  }

  public PageLinkDescriptorBuilder<P, D> permission(String permission) {
    return permission(_ -> permission);
  }

  public IPageLinkDescriptor<P, D> build() {
    return new PageLinkDescriptorImpl<>(
        pageClass, dataDtoFactory, mappingRegistry.build(), permissionProvider);
  }
}
