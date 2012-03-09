package fr.openwide.core.wicket.more.markup.html.list;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.markup.html.feedback.GlobalFeedbackPanel;
import fr.openwide.core.wicket.more.markup.html.link.InvisibleLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox.FancyboxHelper;
import fr.openwide.core.wicket.more.markup.repeater.data.GenericEntityListModelDataProvider;
import fr.openwide.core.wicket.more.markup.repeater.data.OddEvenDataView;


public abstract class AbstractGenericItemListPanel<T extends GenericEntity<Integer, ?>> extends Panel {

	private static final long serialVersionUID = 1L;
	
	private IDataProvider<T> dataProvider;
	
	private DataView<T> dataView;
	
	public AbstractGenericItemListPanel(String id) {
		this(id, (IDataProvider<T>) null);
	}
	
	public AbstractGenericItemListPanel(String id, IModel<? extends List<T>> listModel) {
		this(id, new GenericEntityListModelDataProvider<Integer, T>(listModel));
	}
	
	public AbstractGenericItemListPanel(String id, IDataProvider<T> dataProvider) {
		super(id);
		
		GlobalFeedbackPanel feedbackPanel = new GlobalFeedbackPanel("globalFeedback");
		feedbackPanel.setVisible(isFeedbackEnabled());
		add(feedbackPanel);
		
		this.dataProvider = dataProvider;
	}
	
	public void setModel(IModel<? extends List<T>> listModel) {
		this.dataProvider = new GenericEntityListModelDataProvider<Integer, T>(listModel);
	}
	
	@Override
	public void detachModels() {
		super.detachModels();
		
		if (dataProvider != null) {
			dataProvider.detach();
		}
		if (dataView != null) {
			dataView.detach();
		}
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		dataView = new OddEvenDataView<T>("item", dataProvider, getItemsPerPage()) {
			private static final long serialVersionUID = 8487422965167269031L;
			
			@Override
			protected void populateItem(final Item<T> item) {
				final IModel<T> itemModel = item.getModel();
				
				AbstractGenericItemListPanel.this.addItemColumns(item, itemModel);
				
				Panel actionButtons;
				if (isAtLeastOneActionAvailable()) {
					actionButtons = new AbstractGenericItemListActionButtons<T>("actionButtons", itemModel) {
						private static final long serialVersionUID = 1L;

						@Override
						protected MarkupContainer getActionLink(String id, IModel<? extends T> itemModel) {
							MarkupContainer actionLink = AbstractGenericItemListPanel.this.getActionLink(id, itemModel);
							if (!isActionAvailable()) {
								getActionLinkHidden().setVisible(false);
								return new InvisibleLink(id);
							} else {
								boolean actionVisible = actionLink.isVisible();
								actionLink.setVisible(actionVisible);
								getActionLinkHidden().setVisible(!actionVisible);
							}
							return actionLink;
						}
						
						@Override
						protected Component getEditLink(String id, IModel<? extends T> itemModel) {
							if (!isEditAvailable()) {
								getEditLinkHidden().setVisible(false);
								return new InvisibleLink(id);
							} else {
								Component editLink = AbstractGenericItemListPanel.this.getEditLink(id, itemModel);
								boolean editVisible = editLink.isVisible() &&
									AbstractGenericItemListPanel.this.hasWritePermissionOn(itemModel);
								editLink.setVisible(editVisible);
								getEditLinkHidden().setVisible(!editVisible);
								
								return editLink;
							}
						}

						@Override
						protected IModel<String> getActionIconPath(final IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getActionIconPath(itemModel);
						}

						@Override
						protected IModel<String> getActionText(final IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getActionText(itemModel);
						}
						
						@Override
						protected Component getDeleteLink(String id, IModel<? extends T> itemModel) {
							if (!isDeleteAvailable()) {
								getDeleteLinkHidden().setVisible(false);
								return new InvisibleLink(id);
							} else {
								Component deleteLink = AbstractGenericItemListPanel.this.getDeleteLink(id, itemModel);
								boolean deleteVisible = deleteLink.isVisible() &&
									AbstractGenericItemListPanel.this.hasWritePermissionOn(itemModel);
								deleteLink.setVisible(deleteVisible);
								getDeleteLinkHidden().setVisible(!deleteVisible);
								
								return deleteLink;
							}
						}

						@Override
						protected Component getConfirmationDeleteLink(String id, IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getConfirmationDeleteLink(id, itemModel);
						}

						@Override
						protected Component getCancelDeleteLink(String id, IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getCancelDeleteLink(id, itemModel);
						}
						
					};
				} else {
					actionButtons = new EmptyPanel("actionButtons");
					actionButtons.setVisible(false);
				}
				
				item.add(actionButtons);
			}
		};
		add(dataView);
		
		add(new WebMarkupContainer("emptyList") {
			private static final long serialVersionUID = 6700720373087584498L;

			@Override
			public boolean isVisible() {
				return dataView.getDataProvider().size() == 0;
			}
		});
	}

	protected Component getDeleteLink(String id, IModel<? extends T> itemModel) {
		return new WebMarkupContainer(id);
	}

	protected Component getCancelDeleteLink(String id, IModel<? extends T> itemModel) {
		WebMarkupContainer markup = new WebMarkupContainer(id);
		markup.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			
			private static final long serialVersionUID = -4265950097064531551L;

			@Override
			public JsScope callback() {
				return JsScope.quickScope(FancyboxHelper.getClose());
			}
		}));
		return markup;
	}

	protected Component getConfirmationDeleteLink(String id, final IModel<? extends T> itemModel) {
		return new Link<Void>(id) {
			private static final long serialVersionUID = -5469973899447047462L;
			
			@Override
			public void onClick() {
				try {
					AbstractGenericItemListPanel.this.doDeleteItem(itemModel);
					
					if (!getSession().getFeedbackMessages().hasErrorMessageFor(null)) {
						getSession().info(getString("common.success"));
					}
				} catch (Exception e) {
					getSession().error(getString("common.error"));
				}
				
				setResponsePage(AbstractGenericItemListPanel.this.getPage().getClass(),
						AbstractGenericItemListPanel.this.getPage().getPageParameters());
			}
		};
	}
	
	protected abstract boolean hasWritePermissionOn(IModel<?> itemModel);
	
	protected int getItemsPerPage() {
		return Integer.MAX_VALUE;
	}
	
	protected DataView<T> getDataView() {
		return dataView;
	}

	protected abstract void addItemColumns(final Item<T> item, final IModel<? extends T> itemModel);
	
	protected MarkupContainer getActionLink(final String id, final IModel<? extends T> itemModel) {
		return new InvisibleLink(id);
	}
	
	protected IModel<String> getActionText(final IModel<? extends T> itemModel) {
		return Model.of("");
	}

	protected IModel<String> getActionIconPath(final IModel<? extends T> itemModel) {
		return Model.of("static/common/images/icons/magnifier.png");
	}
	
	protected Component getEditLink(final String id, final IModel<? extends T> itemModel) {
		return new InvisibleLink(id);
	}
	
	protected abstract void doDeleteItem(final IModel<? extends T> itemModel) throws ServiceException, SecurityServiceException;
	
	protected boolean isAtLeastOneActionAvailable() {
		return isActionAvailable() || isDeleteAvailable() || isEditAvailable();
	}
	
	protected abstract boolean isActionAvailable();
	
	protected abstract boolean isDeleteAvailable();
	
	protected abstract boolean isEditAvailable();
	
	protected boolean isFeedbackEnabled() {
		return true;
	}
}