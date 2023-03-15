package igloo.cache.wicket.component;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import igloo.bootstrap.modal.AbstractAjaxModalPopupPanel;
import igloo.cache.binding.CacheBindings;
import igloo.cache.monitor.ICacheWrapper;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.model.BindingModel;
import net.sf.ehcache.Cache;

public class ConsoleMaintenanceJcacheCacheEditPopup extends AbstractAjaxModalPopupPanel<ICacheWrapper> {

	private static final long serialVersionUID = 2798613803910080178L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceJcacheCacheEditPopup.class);

	private Component parent;

	private Form<Cache> form;

	public ConsoleMaintenanceJcacheCacheEditPopup(String id, IModel<ICacheWrapper> cacheModel, Component parent) {
		super(id, cacheModel);
		this.parent = parent;
	}

	@Override
	protected Component createHeader(String wicketId) {
		return new CoreLabel(wicketId, BindingModel.of(getModel(), CacheBindings.cache().name()));
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());
		
		form = new Form<>("form");
		body.add(form);
		
		form.add(
			new TextField<>("maxElementsInMemory", BindingModel.of(getModel(), CacheBindings.cache().maxSize()))
				.setRequired(true)
				.setLabel(new ResourceModel("console.maintenance.caches.cacheManager.cache.maxElementsInMemory"))
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
						LOGGER.error("Error modifying cache {}.", ConsoleMaintenanceJcacheCacheEditPopup.this.getModelObject().getName(), e);
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
