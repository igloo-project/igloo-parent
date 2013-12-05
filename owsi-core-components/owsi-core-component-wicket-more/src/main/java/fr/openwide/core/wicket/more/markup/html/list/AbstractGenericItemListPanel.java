package fr.openwide.core.wicket.more.markup.html.list;

import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.IPageableItems;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.link.InvisibleLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmLink;
import fr.openwide.core.wicket.more.markup.repeater.data.GenericEntityListModelDataProvider;
import fr.openwide.core.wicket.more.markup.repeater.data.OddEvenDataView;

public abstract class AbstractGenericItemListPanel<T extends GenericEntity<Long, ?>> extends Panel {

	private static final long serialVersionUID = 1L;

	private IDataProvider<T> dataProvider;

	private DataView<T> dataView;

	public AbstractGenericItemListPanel(String id, long itemsPerPage) {
		this(id, (IDataProvider<T>) null, itemsPerPage);
	}

	public AbstractGenericItemListPanel(String id, IModel<? extends List<T>> listModel, long itemsPerPage) {
		this(id, new GenericEntityListModelDataProvider<Long, T>(listModel), itemsPerPage);
	}

	public AbstractGenericItemListPanel(String id, IDataProvider<T> dataProvider, long itemsPerPage) {
		super(id);
		this.setOutputMarkupId(true);
		
		this.dataProvider = dataProvider;
		
		dataView = new OddEvenDataView<T>("item", dataProvider, itemsPerPage) {
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
								return new InvisibleLink<Void>(id);
							} else {
								boolean actionVisible = actionLink.isVisible();
								actionLink.setVisible(actionVisible);
								getActionLinkHidden().setVisible(!actionVisible);
							}
							return actionLink;
						}

						@Override
						protected MarkupContainer getEditLink(String id, IModel<? extends T> itemModel) {
							if (!isEditAvailable()) {
								getEditLinkHidden().setVisible(false);
								return new InvisibleLink<Void>(id);
							} else {
								MarkupContainer editLink = AbstractGenericItemListPanel.this.getEditLink(id, itemModel);
								boolean editVisible = editLink.isVisible()
										&& AbstractGenericItemListPanel.this.hasWritePermissionOn(itemModel);
								editLink.setVisible(editVisible);
								getEditLinkHidden().setVisible(!editVisible);

								return editLink;
							}
						}

						@Override
						protected MarkupContainer getDeleteLink(String id, IModel<? extends T> itemModel) {
							if (!isDeleteAvailable()) {
								getDeleteLinkHidden().setVisible(false);
								return new InvisibleLink<Void>(id);
							} else {
								MarkupContainer deleteLink = AbstractGenericItemListPanel.this.getDeleteLink(id, itemModel);
								boolean deleteVisible = deleteLink.isVisible()
										&& AbstractGenericItemListPanel.this.hasWritePermissionOn(itemModel);
								deleteLink.setVisible(deleteVisible);
								getDeleteLinkHidden().setVisible(!deleteVisible);

								return deleteLink;
							}
						}

						@Override
						protected IModel<String> getActionText(final IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getActionText(itemModel);
						}

						@Override
						protected IModel<String> getActionBootstrapIconClass(final IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getActionBootstrapIconClass(itemModel);
						}
						
						@Override
						protected IModel<String> getActionBootstrapIconColorClass(final IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getActionBootstrapIconColorClass(itemModel);
						}
						
						@Override
						protected IModel<String> getActionBootstrapColorClass(IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getActionBootstrapColorClass(itemModel);
						}
						
						@Override
						protected IModel<String> getEditText(final IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getEditText(itemModel);
						}

						@Override
						protected IModel<String> getEditBootstrapIconClass(final IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getEditBootstrapIconClass(itemModel);
						}
						
						@Override
						protected IModel<String> getEditBootstrapIconColorClass(final IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getEditBootstrapIconColorClass(itemModel);
						}
						
						@Override
						protected IModel<String> getEditBootstrapColorClass(IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getEditBootstrapColorClass(itemModel);
						}
						
						@Override
						protected IModel<String> getDeleteText(final IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getDeleteText(itemModel);
						}

						@Override
						protected IModel<String> getDeleteBootstrapIconClass(final IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getDeleteBootstrapIconClass(itemModel);
						}
						
						@Override
						protected IModel<String> getDeleteBootstrapIconColorClass(final IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getDeleteBootstrapIconColorClass(itemModel);
						}
						
						@Override
						protected IModel<String> getDeleteBootstrapColorClass(IModel<? extends T> itemModel) {
							return AbstractGenericItemListPanel.this.getDeleteBootstrapColorClass(itemModel);
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

	public void setModel(IModel<? extends List<T>> listModel) {
		this.dataProvider = new GenericEntityListModelDataProvider<Long, T>(listModel);
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

	@SuppressWarnings("unchecked")
	protected MarkupContainer getDeleteLink(String id, final IModel<? extends T> itemModel) {
		return new AjaxConfirmLink<T>(id, (IModel<T>) itemModel, getDeleteConfirmationTitleModel(itemModel),
				getDeleteConfirmationTextModel(itemModel), getDeleteConfirmationYesLabelModel(itemModel),
				getDeleteConfirmationNoLabelModel(itemModel)) {

			private static final long serialVersionUID = -5179621361619239269L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					doDeleteItem(itemModel);
					Session.get().success(getString("common.delete.success"));
				} catch (Exception e) {
					Session.get().error(getString("common.delete.error"));
				}
				target.add(getPage());
				dataProvider.detach();
				FeedbackUtils.refreshFeedback(target, getPage());
			}
		};
	}

	protected IModel<String> getDeleteConfirmationTitleModel(IModel<? extends T> itemModel) {
		return new ResourceModel("common.confirmTitle");
	}

	protected IModel<String> getDeleteConfirmationTextModel(IModel<? extends T> itemModel) {
		return new ResourceModel("common.deleteConfirmation");
	}

	protected IModel<String> getDeleteConfirmationYesLabelModel(IModel<? extends T> itemModel) {
		return new ResourceModel("common.confirm");
	}

	protected IModel<String> getDeleteConfirmationNoLabelModel(IModel<? extends T> itemModel) {
		return new ResourceModel("common.cancel");
	}

	protected abstract boolean hasWritePermissionOn(IModel<? extends T> itemModel);

	protected DataView<T> getDataView() {
		return dataView;
	}
	
	public IPageableItems getPageable() {
		return dataView;
	}

	protected abstract void addItemColumns(final Item<T> item, final IModel<? extends T> itemModel);

	protected MarkupContainer getActionLink(final String id, final IModel<? extends T> itemModel) {
		return new InvisibleLink<Void>(id);
	}

	protected IModel<String> getActionText(final IModel<? extends T> itemModel) {
		return new ResourceModel("common.portfolio.action.viewDetails");
	}

	protected IModel<String> getActionBootstrapIconClass(final IModel<? extends T> itemModel) {
		return Model.of("icon-search fa fa-search fa-fw");
	}

	/**
	 * <code>icon-white</code> ou rien
	 * 
	 * @param itemModel
	 * @return
	 */
	protected IModel<String> getActionBootstrapIconColorClass(IModel<? extends T> itemModel) {
		return Model.of("icon-white");
	}

	/**
	 * <code>btn-primary</code>, <code>btn-danger</code>, <code>btn-success</code>, etc.
	 * 
	 * @param itemModel
	 * @return
	 */
	protected IModel<String> getActionBootstrapColorClass(IModel<? extends T> itemModel) {
		return Model.of("btn-primary");
	}

	protected MarkupContainer getEditLink(final String id, final IModel<? extends T> itemModel) {
		return new InvisibleLink<Void>(id);
	}
	
	protected IModel<String> getEditText(final IModel<? extends T> itemModel) {
		return new ResourceModel("common.itemList.action.edit");
	}

	protected IModel<String> getEditBootstrapIconClass(final IModel<? extends T> itemModel) {
		return Model.of("icon-pencil fa fa-pencil fa-fw");
	}
	
	/**
	 * <code>icon-white</code> ou rien
	 * 
	 * @param itemModel
	 * @return
	 */
	protected IModel<String> getEditBootstrapIconColorClass(IModel<? extends T> itemModel) {
		return Model.of("icon-white");
	}

	/**
	 * <code>btn-primary</code>, <code>btn-danger</code>, <code>btn-success</code>, etc.
	 * 
	 * @param itemModel
	 * @return
	 */
	protected IModel<String> getEditBootstrapColorClass(IModel<? extends T> itemModel) {
		return Model.of("btn-primary");
	}
	
	protected IModel<String> getDeleteText(final IModel<? extends T> itemModel) {
		return new ResourceModel("common.itemList.action.delete");
	}

	protected IModel<String> getDeleteBootstrapIconClass(final IModel<? extends T> itemModel) {
		return Model.of("icon-trash fa fa-trash-o fa-fw");
	}
	
	/**
	 * <code>icon-white</code> ou rien
	 * 
	 * @param itemModel
	 * @return
	 */
	protected IModel<String> getDeleteBootstrapIconColorClass(IModel<? extends T> itemModel) {
		return Model.of("icon-white");
	}

	/**
	 * <code>btn-primary</code>, <code>btn-danger</code>, <code>btn-success</code>, etc.
	 * 
	 * @param itemModel
	 * @return
	 */
	protected IModel<String> getDeleteBootstrapColorClass(IModel<? extends T> itemModel) {
		return Model.of("btn-danger");
	}

	protected abstract void doDeleteItem(final IModel<? extends T> itemModel) throws ServiceException,
			SecurityServiceException;

	protected boolean isAtLeastOneActionAvailable() {
		return isActionAvailable() || isDeleteAvailable() || isEditAvailable();
	}

	protected abstract boolean isActionAvailable();

	protected abstract boolean isDeleteAvailable();

	protected abstract boolean isEditAvailable();
}