package org.iglooproject.bootstrap.api;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface IAjaxModalShowListener {
	
	void onShow(IAjaxModalPopupPanel modal, AjaxRequestTarget target);

}
