package org.iglooproject.wicket.api.download;

import java.io.File;
import java.util.Objects;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.model.ReadOnlyModel;
import org.iglooproject.wicket.api.util.Detachables;
import org.javatuples.LabelValue;

public class FileDeferredDownloadBehavior extends AbstractDeferredDownloadBehavior {

	private static final long serialVersionUID = -2564879404137129896L;

	private final IModel<LabelValue<String, File>> fileInformationModel;

	public FileDeferredDownloadBehavior(IModel<File> fileModel, IModel<String> fileNameModel) {
		this(fileModel, fileNameModel, true);
	}

	public FileDeferredDownloadBehavior(final IModel<File> fileModel, final IModel<String> fileNameModel, boolean addAntiCache) {
		this(
				new IModel<LabelValue<String, File>>() {
					private static final long serialVersionUID = 1L;
					@Override
					public LabelValue<String, File> getObject() {
						return LabelValue.with(fileNameModel.getObject(), fileModel.getObject());
					}
					@Override
					public void detach() {
						IModel.super.detach();
						Detachables.detach(
							fileModel,
							fileNameModel
						);
					}
				},
				addAntiCache
		);
		Objects.requireNonNull(fileModel);
		Objects.requireNonNull(fileNameModel);
	}

	public FileDeferredDownloadBehavior(IModel<LabelValue<String, File>> fileInformationModel) {
		this(fileInformationModel, true);
	}

	public FileDeferredDownloadBehavior(IModel<LabelValue<String, File>> fileInformationModel, boolean addAntiCache) {
		super(
				ReadOnlyModel.<LabelValue<String, File>, File>of(
						fileInformationModel,
						input -> input != null ? input.getValue() : null
				),
				addAntiCache
		);
		this.fileInformationModel = Objects.requireNonNull(fileInformationModel);
	}

	@Override
	protected String getFileDisplayName() {
		return fileInformationModel.getObject().getLabel();
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(fileInformationModel);
	}

}