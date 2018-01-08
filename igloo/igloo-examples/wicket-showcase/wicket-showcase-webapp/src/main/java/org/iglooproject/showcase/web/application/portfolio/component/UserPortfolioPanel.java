package org.iglooproject.showcase.web.application.portfolio.component;

import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.showcase.core.business.user.model.User;
import org.iglooproject.showcase.core.business.user.service.IUserService;
import org.iglooproject.showcase.core.util.binding.Bindings;
import org.iglooproject.showcase.web.application.portfolio.page.UserDescriptionPage;
import org.iglooproject.wicket.more.markup.html.action.AbstractOneParameterAjaxAction;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRendererInformation;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.multivaluedexpand.MultivaluedExpandBehavior;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.iglooproject.wicket.more.util.model.Models;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Functions;

public class UserPortfolioPanel extends Panel {
	
	private static final long serialVersionUID = 6906542421342609922L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPortfolioPanel.class);

	@SpringBean
	private IUserService userService;

	private final IDataProvider<User> dataProvider;

	private final DecoratedCoreDataTablePanel<User, ISort<?>> dataTable;

	public UserPortfolioPanel(String id, IDataProvider<User> dataProvider, int itemsPerPage) {
		super(id);
		setOutputMarkupId(true);
		
		this.dataProvider = dataProvider;
		
		dataTable =
				DataTableBuilder.start(dataProvider)
						.addLabelColumn(new ResourceModel("user.portfolio.header.lastName"), Bindings.user().lastName())
								.withLink(UserDescriptionPage.MAPPER)
						.addLabelColumn(new ResourceModel("user.portfolio.header.firstName"), Bindings.user().firstName())
								.withLink(UserDescriptionPage.MAPPER)
						.addLabelColumn(new ResourceModel("user.portfolio.header.userName"), Bindings.user().userName())
								.withLink(UserDescriptionPage.MAPPER)
						.addLabelColumn(new ResourceModel("user.portfolio.header.email"), Bindings.user().email())
						.addColumn(
								new AbstractCoreColumn<User, ISort<?>>(new ResourceModel("user.portfolio.header.tags")) {
									private static final long serialVersionUID = 1L;
									@Override
									public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
										cellItem
												.add(new TagsFragment(componentId, rowModel))
												.add(new MultivaluedExpandBehavior());
									}
								}
						)
								.withClass("multivalued with-separator")
						.addBootstrapLabelColumn(new ResourceModel("user.portfolio.header.active"), Functions.identity(), new BootstrapRenderer<User>() {
							private static final long serialVersionUID = 1L;
							@Override
							protected BootstrapRendererInformation doRender(User value, Locale locale) {
								return BootstrapRendererInformation.builder()
										.label(value.isActive() ? "active" : "inactive")
										.icon(value.isActive() ? "fa fa-fw fa-check" : "fa fa-fw fa-remove")
										.color(value.isActive() ? BootstrapColor.SUCCESS : BootstrapColor.DANGER)
										.build();
							}
						})
						.addActionColumn()
								.addConfirmAction(BootstrapRenderer.constant("common.itemList.action.delete", "fa fa-fw fa-times", BootstrapColor.DANGER))
										.title(new ResourceModel("common.confirmTitle"))
										.content(new ResourceModel("common.deleteConfirmation"))
										.confirm()
										.onClick(new AbstractOneParameterAjaxAction<IModel<User>>() {
											private static final long serialVersionUID = 1L;
											@Override
											public void execute(AjaxRequestTarget target, IModel<User> parameter) {
												try {
													userService.delete(parameter.getObject());
													Session.get().success(getString("common.delete.success"));
													target.add(getPage());
												} catch (Exception e) {
													LOGGER.error("Erreur durant la suppression d'un utilisateur.", e);
													Session.get().error(getString("common.delete.error"));
												}
												dataProvider.detach();
												FeedbackUtils.refreshFeedback(target, getPage());
											}
										})
										.hideLabel()
								.withClassOnElements("btn-xs")
								.end()
				.withNoRecordsResourceKey("common.emptyList")
				.decorate()
						.count("user.portfolio.userCount")
						.pagers()
				.build("results", itemsPerPage);
		
		add(
				dataTable
		);
	}

	private class TagsFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		public TagsFragment(String id, IModel<User> model) {
			super(id, "tagsFragment", UserPortfolioPanel.this, model);
			
			add(
					new CollectionView<String>(
							"tags",
							BindingModel.of(model, Bindings.user().tags()),
							Models.<String>serializableModelFactory()
					) {
						private static final long serialVersionUID = 1L;
						
						@Override
						protected void populateItem(Item<String> tagItem) {
							tagItem.add(new Label("tag", tagItem.getModel()));
						}
					}
			);
		}
		
	}

	public DecoratedCoreDataTablePanel<User, ISort<?>> getDataTable() {
		return dataTable;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(dataProvider);
	}

}
