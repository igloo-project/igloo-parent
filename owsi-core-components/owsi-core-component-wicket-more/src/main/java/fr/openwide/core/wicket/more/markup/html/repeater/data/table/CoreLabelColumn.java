package fr.openwide.core.wicket.more.markup.html.repeater.data.table;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.markup.html.panel.InvisiblePanel;
import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.factory.LinkGeneratorFactory;

public abstract class CoreLabelColumn<T, S extends ISort<?>> extends AbstractCoreColumn<T, S> {

	private static final long serialVersionUID = -8588236421923900497L;
	
	private boolean multiline = false;
	
	private boolean showPlaceholder = false;
	
	private IModel<String> placeholderModel = null;
	
	private LinkGeneratorFactory<T> linkGeneratorFactory;
	
	private LinkGeneratorFactory<T> sideLinkGeneratorFactory;
	
	private boolean disableIfInvalid = false;
	
	private boolean hideIfInvalid = false;
	
	public CoreLabelColumn(IModel<String> displayModel) {
		super(displayModel);
	}
	
	@Override
	public void detach() {
		super.detach();
		if (placeholderModel != null) {
			placeholderModel.detach();
		}
		if (linkGeneratorFactory != null) {
			linkGeneratorFactory.detach();
		}
		if (sideLinkGeneratorFactory != null) {
			sideLinkGeneratorFactory.detach();
		}
	}

	@Override
	public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
		cellItem.add(
				new CoreLabelLinkColumnPanel<T, S>(componentId, rowModel) {
					private static final long serialVersionUID = 1L;
					@Override
					public CoreLabel getLabel(String wicketId, IModel<T> rowModel) {
						return decorate(newLabel(wicketId, rowModel));
					}
					@Override
					public MarkupContainer getLink(String wicketId, IModel<T> rowModel) {
						if (linkGeneratorFactory != null) {
							return decorate(linkGeneratorFactory.create(rowModel).link(wicketId));
						}
						return new InvisiblePanel(wicketId);
					}
					@Override
					public MarkupContainer getSideLink(String wicketId, IModel<T> rowModel) {
						if (sideLinkGeneratorFactory != null) {
							return decorate(sideLinkGeneratorFactory.create(rowModel).link(wicketId))
									.add(new WebMarkupContainer("sideLinkIcon").add(new ClassAttributeAppender("fa fa-share-square-o")));
						}
						return new InvisiblePanel(wicketId);
					}
				}.setRenderBodyOnly(true) // Delete useless <div> under <td>. If it could be usefull, give the ability to configure it
		);
	}

	protected abstract CoreLabel newLabel(String componentId, IModel<T> rowModel);

	private CoreLabel decorate(CoreLabel label) {
		if (multiline) {
			label.multiline();
		}
		if (showPlaceholder) {
			if (placeholderModel != null) {
				label.showPlaceholder(placeholderModel);
			} else {
				label.showPlaceholder();
			}
		}
		return label;
	}
	
	private AbstractDynamicBookmarkableLink decorate(AbstractDynamicBookmarkableLink link) {
		if (disableIfInvalid) {
			link.disableIfInvalid();
		}
		if (hideIfInvalid) {
			link.hideIfInvalid();
		}
		return link;
	}

	public CoreLabelColumn<T, S> multiline() {
		this.multiline = true;
		return this;
	}

	public CoreLabelColumn<T, S> showPlaceholder() {
		this.showPlaceholder = true;
		return this;
	}

	public IModel<String> getPlaceholderModel() {
		return placeholderModel;
	}

	public CoreLabelColumn<T, S> showPlaceholder(IModel<String> placeholderModel) {
		this.showPlaceholder = true;
		this.placeholderModel = placeholderModel;
		return this;
	}

	public LinkGeneratorFactory<T> getLinkGeneratorFactory() {
		return linkGeneratorFactory;
	}

	public CoreLabelColumn<T, S> setLinkGeneratorFactory(LinkGeneratorFactory<T> linkGeneratorFactory) {
		this.linkGeneratorFactory = linkGeneratorFactory;
		return this;
	}

	public LinkGeneratorFactory<T> getSideLinkGeneratorFactory() {
		return sideLinkGeneratorFactory;
	}

	public CoreLabelColumn<T, S> setSideLinkGeneratorFactory(LinkGeneratorFactory<T> sideLinkGeneratorFactory) {
		this.sideLinkGeneratorFactory = sideLinkGeneratorFactory;
		return this;
	}

	public CoreLabelColumn<T, S> disableIfInvalid() {
		this.disableIfInvalid = true;
		return this;
	}

	public CoreLabelColumn<T, S> hideIfInvalid() {
		this.hideIfInvalid = true;
		return this;
	}

}
