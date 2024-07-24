package org.igloo.storage.integration.wicket;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.FichierBinding;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.model.CorePermissionConstants;
import org.iglooproject.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.IOneMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class FichierFileStorageWebResource extends AbstractFichierStoreWebResource {

  private static final long serialVersionUID = 4055986406247245615L;
  private static final FichierBinding FICHIER = new FichierBinding();

  @SpringBean private IStorageService storageService;

  private static final ResourceReference REFERENCE_ATTACHMENT =
      new ResourceReference(
          FichierFileStorageWebResource.class, "FichierAttachmmentFileStoreWebResource") {
        private static final long serialVersionUID = 1L;

        @Override
        public IResource getResource() {
          FichierFileStorageWebResource resource = new FichierFileStorageWebResource();
          resource.setContentDisposition(ContentDisposition.ATTACHMENT);
          return resource;
        }
      };

  private static final IOneMappableParameterMainState<
          Fichier, IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor>
      MAPPER_BUILDER =
          LinkDescriptorBuilder.start()
              .model(Fichier.class)
              .map(CommonParameters.ID)
              .mandatory()
              .permission(CorePermissionConstants.READ)
              .renderInUrl(CommonParameters.TIMESTAMP, FICHIER.creationDate())
              .optional();

  private static final ResourceReference REFERENCE =
      new ResourceReference(FichierFileStorageWebResource.class, "FichierFileStorageWebResource") {
        private static final long serialVersionUID = 1L;

        @Override
        public IResource getResource() {
          return new FichierFileStorageWebResource();
        }
      };

  public static final IOneParameterLinkDescriptorMapper<IResourceLinkDescriptor, Fichier> MAPPER =
      MAPPER_BUILDER.resource(REFERENCE);

  public static final IOneParameterLinkDescriptorMapper<IImageResourceLinkDescriptor, Fichier>
      MAPPER_ATTACHMENT = MAPPER_BUILDER.imageResource(REFERENCE_ATTACHMENT);

  public FichierFileStorageWebResource() {
    this(data -> {});
  }

  public FichierFileStorageWebResource(
      SerializableConsumer<ResourceResponse> resourceResponseConsumer) {
    super(resourceResponseConsumer);
    disableCaching();
  }

  @Override
  protected FichierStoreResourceStream getFileStoreResourceStream(PageParameters parameters)
      throws ServiceException, SecurityServiceException {
    IModel<Fichier> fichierModel = new GenericEntityModel<>();

    try {
      MAPPER.map(fichierModel).extract(parameters);
    } catch (LinkParameterValidationException e) {
      throw new LinkParameterValidationRuntimeException(e);
    }

    Fichier fichier = fichierModel.getObject();
    try {
      File file = storageService.getFile(fichier);
      return new FichierStoreResourceStream(
          file, Optional.ofNullable(fichier.getFilename()).orElse(""));
    } catch (FileNotFoundException e) {
      throw new ServiceException(e);
    }
  }

  public static final ResourceReference get() {
    return REFERENCE;
  }

  public static final ResourceReference attachment() {
    return REFERENCE_ATTACHMENT;
  }
}
