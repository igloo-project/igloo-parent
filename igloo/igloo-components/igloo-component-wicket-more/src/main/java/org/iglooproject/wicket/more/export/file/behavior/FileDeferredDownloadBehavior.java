package org.iglooproject.wicket.more.export.file.behavior;

import java.io.File;
import java.util.Objects;

import org.apache.wicket.Component;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.javatuples.LabelValue;

import org.iglooproject.commons.util.functional.SerializableFunction;
import org.iglooproject.wicket.more.export.AbstractDeferredDownloadBehavior;
import org.iglooproject.wicket.more.model.ReadOnlyModel;
import org.iglooproject.wicket.more.util.model.Detachables;

public class FileDeferredDownloadBehavior extends AbstractDeferredDownloadBehavior {

	private static final long serialVersionUID = -2564879404137129896L;

	private final IModel<LabelValue<String, File>> fileInformationModel;

	public FileDeferredDownloadBehavior(IModel<File> fileModel, IModel<String> fileNameModel) {
		this(fileModel, fileNameModel, true);
	}

	public FileDeferredDownloadBehavior(final IModel<File> fileModel, final IModel<String> fileNameModel, boolean addAntiCache) {
		this(
				new AbstractReadOnlyModel<LabelValue<String, File>>() {
					private static final long serialVersionUID = 1L;
					@Override
					public LabelValue<String, File> getObject() {
						return LabelValue.with(fileNameModel.getObject(), fileModel.getObject());
					}
					@Override
					public void detach() {
						super.detach();
						Detachables.detach(fileModel, fileNameModel);
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
				ReadOnlyModel.of(
						fileInformationModel,
						new SerializableFunction<LabelValue<String, File>, File>() {
							private static final long serialVersionUID = 1L;
							@Override
							public File apply(LabelValue<String, File> input) {
								return input != null ? input.getValue() : null;
							}
						}
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