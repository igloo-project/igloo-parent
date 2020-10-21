package org.iglooproject.wicket.bootstrap4.console.maintenance.ehcache.component;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.console.maintenance.ehcache.model.EhCacheCacheInformation;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.DelegatedMarkupPanel;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.util.binding.CoreWicketMoreBindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.Cache;

public class EhCacheCacheEditPopup extends AbstractAjaxModalPopupPanel<EhCacheCacheInformation> {

	private static final long serialVersionUID = 2798613803910080178L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EhCacheCacheEditPopup.class);

	private Component parent;

	private Form<Cache> form;

	public EhCacheCacheEditPopup(String id, IModel<EhCacheCacheInformation> cacheModel, Component parent) {
		super(id, cacheModel);
		this.parent = parent;
	}

	@Override
	protected Component createHeader(String wicketId) {
		return new CoreLabel(wicketId, BindingModel.of(getModel(), CoreWicketMoreBindings.ehCacheCacheInformation().name()));
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());
		
		form = new Form<>("form");
		body.add(form);

		form.add(
			new TextField<>("maxElementsInMemory", BindingModel.of(getModel(), CoreWicketMoreBindings.ehCacheCacheInformation().maxElementsInMemory()))
				.setRequired(true)
				.setLabel(new ResourceModel("console.maintenance.ehcache.cacheManager.cache.maxElementsInMemory"))
		);
		
		return body;
	}

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, getClass());
		
		footer.add(
			new AjaxButton("save", form) {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					try {
						Session.get().success(getString("common.success"));
						closePopup(target);
						target.add(parent);
					} catch (Exception e) {
						LOGGER.error("Erreur lors de la modification d'un cache.", e);
						Session.get().error(getString("common.error.unexpected"));
					}
					
					FeedbackUtils.refreshFeedback(target, getPage());
				}
				
				@Override
				protected void onError(AjaxRequestTarget target) {
					FeedbackUtils.refreshFeedback(target, getPage());
				}
			}
		);
		
		BlankLink cancel = new BlankLink("cancel");
		addCancelBehavior(cancel);
		footer.add(cancel);
		
		return footer;
	}

	

}
