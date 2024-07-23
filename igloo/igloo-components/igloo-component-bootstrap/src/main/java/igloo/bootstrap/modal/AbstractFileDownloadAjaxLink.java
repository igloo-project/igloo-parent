package igloo.bootstrap.modal;

import igloo.wicket.download.FileDeferredDownloadBehavior;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.model.Detachables;
import java.io.File;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.javatuples.LabelValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFileDownloadAjaxLink extends AjaxLink<Void> {

  private static final long serialVersionUID = 3043111209219249583L;

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileDownloadAjaxLink.class);

  private final WorkInProgressPopup loadingPopup;

  private final IModel<LabelValue<String, File>> tempFileInformationModel;

  private final FileDeferredDownloadBehavior ajaxDownload;

  public AbstractFileDownloadAjaxLink(String id, WorkInProgressPopup loadingPopup) {
    super(id);
    this.loadingPopup = loadingPopup;
    this.tempFileInformationModel = new Model<>();

    this.ajaxDownload = new FileDeferredDownloadBehavior(tempFileInformationModel);

    add(ajaxDownload);
  }

  @Override
  protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
    super.updateAjaxAttributes(attributes);
    loadingPopup.updateAjaxAttributes(attributes);
  }

  @Override
  public void onClick(AjaxRequestTarget target) {
    try {
      tempFileInformationModel.setObject(generateFileInformation());
      ajaxDownload.initiate(target);
      target.appendJavaScript(loadingPopup.closeStatement().render());
    } catch (Exception e) {
      tempFileInformationModel.setObject(null);
      LOGGER.error("Error while downloading a file.", e);
      error(getString("common.error.unexpected"));
    }
    FeedbackUtils.refreshFeedback(target, getPage());
  }

  protected abstract LabelValue<String, File> generateFileInformation();

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(tempFileInformationModel);
  }
}
