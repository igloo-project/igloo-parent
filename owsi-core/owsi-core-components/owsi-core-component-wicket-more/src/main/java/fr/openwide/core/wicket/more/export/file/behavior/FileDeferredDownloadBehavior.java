package fr.openwide.core.wicket.more.export.file.behavior;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import fr.openwide.core.commons.util.mime.MediaType;
import fr.openwide.core.wicket.more.export.AbstractDeferredDownloadBehavior;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.util.binding.CoreWicketMoreBindings;
import fr.openwide.core.wicket.more.util.model.Detachables;

public class FileDeferredDownloadBehavior extends AbstractDeferredDownloadBehavior {

	private static final long serialVersionUID = -2564879404137129896L;

	public static final String FILE_NAME_DATE_PATTERN_KEY = "common.action.export.file.datePattern";

	private final IModel<String> fileDisplayNamePrefixModel;

	private final IModel<String> extensionModel;

	public static FileDeferredDownloadBehavior with(IModel<File> tempFileModel, IModel<String> fileDisplayNamePrefixModel, IModel<String> extensionModel) {
		return with(tempFileModel, fileDisplayNamePrefixModel, extensionModel, true);
	}

	public static FileDeferredDownloadBehavior with(IModel<File> tempFileModel, IModel<String> fileDisplayNamePrefixModel, IModel<String> extensionModel, boolean addAntiCache) {
		return new FileDeferredDownloadBehavior(tempFileModel, fileDisplayNamePrefixModel, extensionModel, addAntiCache);
	}

	public static FileDeferredDownloadBehavior withMediaType(IModel<File> tempFileModel, IModel<String> fileDisplayNamePrefixModel, IModel<MediaType> mediaTypeModel) {
		return withMediaType(tempFileModel, fileDisplayNamePrefixModel, mediaTypeModel, true);
	}

	public static FileDeferredDownloadBehavior withMediaType(IModel<File> tempFileModel, IModel<String> fileDisplayNamePrefixModel, IModel<MediaType> mediaTypeModel, boolean addAntiCache) {
		return new FileDeferredDownloadBehavior(tempFileModel, fileDisplayNamePrefixModel, BindingModel.of(mediaTypeModel, CoreWicketMoreBindings.mediaType().extension()), addAntiCache);
	}

	private FileDeferredDownloadBehavior(IModel<File> tempFileModel, IModel<String> fileDisplayNamePrefixModel, IModel<String> extensionModel, boolean addAntiCache) {
		super(tempFileModel, addAntiCache);
		this.fileDisplayNamePrefixModel = fileDisplayNamePrefixModel;
		this.extensionModel = extensionModel;
	}

	@Override
	protected String getFileDisplayName() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(getComponent().getString(FILE_NAME_DATE_PATTERN_KEY));
		return new StringBuilder(fileDisplayNamePrefixModel.getObject())
				.append(dateFormat.format(new Date()))
				.append(".")
				.append(extensionModel.getObject())
				.toString();
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(fileDisplayNamePrefixModel, extensionModel);
	}

}