package igloo.bootstrap.modal;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface IAjaxModalShowListener {
	
	void onShow(IAjaxModalPopupPanel modal, AjaxRequestTarget target);

}
