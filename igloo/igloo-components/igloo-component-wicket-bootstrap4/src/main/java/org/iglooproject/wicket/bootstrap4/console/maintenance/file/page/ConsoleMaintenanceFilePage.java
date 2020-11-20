package org.iglooproject.wicket.bootstrap4.console.maintenance.file.page;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.RangeValidator;
import org.iglooproject.jpa.more.business.file.model.path.HashTableFileStorePathGeneratorImpl;
import org.iglooproject.wicket.bootstrap4.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleMaintenanceFilePage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = -4594419424463767339L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceFilePage.class);

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
			.page(ConsoleMaintenanceFilePage.class);
	}

	private final IModel<Integer> hashTableByteSizeModel = new Model<>(1);
	private final IModel<String> fileKeyModel = new Model<>();
	private final IModel<String> extensionModel = new Model<>();
	
	private final IModel<String> pathModel = new Model<>();
	
	public ConsoleMaintenanceFilePage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(
			new ResourceModel("console.maintenance.file"),
			ConsoleMaintenanceFilePage.linkDescriptor()
		));
		
		add(
			new Form<Void>("form") {
				private static final long serialVersionUID = 1L;
				@Override
				protected void onSubmit() {
					try {
						HashTableFileStorePathGeneratorImpl pathGenerator = new HashTableFileStorePathGeneratorImpl(hashTableByteSizeModel.getObject());
						pathModel.setObject(pathGenerator.getFilePath(fileKeyModel.getObject(), extensionModel.getObject()));
						pathModel.detach();
					} catch (Exception e) {
						LOGGER.error("Unexpected error while generating file path through hashtable.");
						pathModel.setObject(null);
						Session.get().error(getString("common.error.unexpected"));
					}
				}
				@Override
				protected void onError() {
					pathModel.setObject(null);
				}
			}
				.add(
					new TextField<>("hashTableByteSize", hashTableByteSizeModel, Integer.class)
						.setRequired(true)
						.setLabel(new ResourceModel("console.maintenance.file.pathGeneration.hashTableByteSize"))
						.add(
							new RangeValidator<>(
								HashTableFileStorePathGeneratorImpl.MIN_HASH_TABLE_BYTE_SIZE,
								HashTableFileStorePathGeneratorImpl.MAX_HASH_TABLE_BYTE_SIZE
							)
						),
					new TextField<>("fileKey", fileKeyModel)
						.setRequired(true)
						.setLabel(new ResourceModel("console.maintenance.file.pathGeneration.fileKey")),
					new TextField<>("extension", extensionModel)
						.setLabel(new ResourceModel("console.maintenance.file.pathGeneration.extension")),
					
					new CoreLabel("path", pathModel).hideIfEmpty()
				)
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(
			hashTableByteSizeModel,
			fileKeyModel,
			extensionModel,
			pathModel
		);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ConsoleMaintenanceFilePage.class;
	}

}
