package org.iglooproject.wicket.more.bindable.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.IFormModelUpdateListener;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;
import org.iglooproject.wicket.more.bindable.model.IBindableModel;

import com.google.common.base.Objects;

import igloo.bootstrap.modal.IAjaxModalPopupPanel;
import igloo.bootstrap.modal.IAjaxModalShowListener;

/**
 * A panel that uses bindable models for sub-components models, in order to leverage caching capabilities, and resets
 * those caches automatically when necessary.
 * 
 * <p>This type of panel is especially useful inside editing popup windows, where:
 * <ul>
 * <li>the root model may change just before opening the popup window, and thus the caches may have to be reset
 * automatically on show.
 * <li>the caches may have to be written to the root object just before submitting the form.
 * </ul>
 */
public class AbstractCachingPanel<T> extends GenericPanel<T>
		implements IFormModelUpdateListener, IAjaxModalShowListener {

	private static final long serialVersionUID = -61926721790962461L;
	
	private final IBindableModel<T> bindableModel;
	
	private final IModel<T> lastRootModel;
	
	public AbstractCachingPanel(String id, IBindableModel<T> model, IModel<T> lastRootModel) {
		super(id, model);
		this.bindableModel = model;
		this.lastRootModel = lastRootModel;
	}

	protected final <T2> IBindableModel<T2> bind(BindingRoot<T, T2> binding) {
		return bindableModel.bind(binding);
	}

	protected final <T2> IBindableModel<T2> bindWithCache(BindingRoot<T, T2> binding, IModel<T2> workingCopyProposal) {
		return bindableModel.bindWithCache(binding, workingCopyProposal);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		lastRootModel.detach();
	}
	
	@Override
	protected void onConfigure() {
		T modelObject = getModelObject();
		if (!Objects.equal(lastRootModel.getObject(), modelObject)) {
			lastRootModel.setObject(modelObject);
			readCache();
		}
		super.onConfigure();
	}
	
	protected final void readCache() {
		bindableModel.readAll();
	}
	
	protected final void writeCache() {
		bindableModel.writeAll();
	}

	@Override
	public void onShow(IAjaxModalPopupPanel modal, AjaxRequestTarget target) {
		readCache();
	}

	@Override
	public void updateModel() {
		writeCache();
	}

}
