package org.iglooproject.wicket.bootstrap3.console.maintenance.ehcache.component;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
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

public class EhCacheCacheModificationPanel extends AbstractAjaxModalPopupPanel<EhCacheCacheInformation> {

	private static final long serialVersionUID = 2798613803910080178L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EhCacheCacheModificationPanel.class);
	
	private Form<Cache> cacheForm;
	
	private TextField<Long> maxSizeField;
	
	private Component parent;
	
	public EhCacheCacheModificationPanel(String id, IModel<EhCacheCacheInformation> cacheModel, Component parent) {
		super(id, cacheModel);
		
		this.parent = parent;
	}

	@Override
	protected Component createHeader(String wicketId) {
		return new Label(wicketId, BindingModel.of(getModel(), CoreWicketMoreBindings.ehCacheCacheInformation().name()));
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, EhCacheCacheModificationPanel.class);
		
		cacheForm = new Form<>("cacheForm");
		maxSizeField = new TextField<>("maxSize", BindingModel.of(getModel(),
				CoreWicketMoreBindings.ehCacheCacheInformation().maxElementsInMemory()));
		maxSizeField.setLabel(new ResourceModel("console.maintenance.ehcache.portfolio.max"));
		cacheForm.add(maxSizeField);
		body.add(cacheForm);
		
		return body;
	}

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, EhCacheCacheModificationPanel.class);
		
		// Bouton valider
		AjaxButton valider = new AjaxButton("validerModificationCache", cacheForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				try {
					Long maxSize = maxSizeField.getModelObject();
					EhCacheCacheModificationPanel.this.getModelObject().setMaxElementsInMemory(maxSize);
					Session.get().success(getString("console.maintenance.ehcache.cacheManager.cache.message.ok"));
					closePopup(target);
					target.add(parent);
				} catch (Exception e) {
					LOGGER.error("Erreur lors de la modification d'un cache.", e);
					Session.get().error(getString("console.maintenance.ehcache.cacheManager.cache.message.erreur"));
				}
				
				FeedbackUtils.refreshFeedback(target, getPage());
			}
			
			@Override
			protected void onError(AjaxRequestTarget target) {
				FeedbackUtils.refreshFeedback(target, getPage());
			}
		};
		footer.add(valider);
		
		BlankLink annuler = new BlankLink("annulerModificationCache");
		addCancelBehavior(annuler);
		footer.add(annuler);
		
		return footer;
	}

	@Override
	public IModel<String> getModalCssClassModel() {
		return Model.of("modal-ehcache-modification");
	}

}
