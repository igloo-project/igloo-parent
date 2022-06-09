package igloo.bootstrap.modal;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface IAjaxModalPopupPanel extends IModalPopupPanel {

	void show(AjaxRequestTarget target);

}
