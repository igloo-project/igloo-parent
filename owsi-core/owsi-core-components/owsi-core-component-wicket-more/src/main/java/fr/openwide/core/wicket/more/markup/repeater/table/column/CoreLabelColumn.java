package fr.openwide.core.wicket.more.markup.repeater.table.column;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.markup.html.panel.InvisiblePanel;
import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.rendering.Renderer;

public abstract class CoreLabelColumn<T, S extends ISort<?>> extends AbstractCoreColumn<T, S> {

	private static final long serialVersionUID = -8588236421923900497L;
	
	private boolean multiline = false;
	
	private boolean showPlaceholder = false;
	
	private IModel<String> placeholderModel = null;
	
	private Renderer<? super T> tooltipRenderer;
	
	private IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> linkGeneratorMapper;
	
	private IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> sideLinkGeneratorMapper;
	
	private boolean hideIfInvalid = false;
	
	private List<Behavior> linkBehaviors = Lists.newArrayList();
	
	public CoreLabelColumn(IModel<String> displayModel) {
		super(displayModel);
	}
	
	@Override
	public void detach() {
		super.detach();
		if (placeholderModel != null) {
			placeholderModel.detach();
		}
		if (linkGeneratorMapper != null) {
			linkGeneratorMapper.detach();
		}
		if (sideLinkGeneratorMapper != null) {
			sideLinkGeneratorMapper.detach();
		}
	}

	@Override
	public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
		cellItem.add(
				new CoreLabelLinkColumnPanel<T, S>(componentId, rowModel) {
					private static final long serialVersionUID = 1L;
					@Override
					public CoreLabel getLabel(String wicketId, IModel<T> rowModel) {
						return decorate(newLabel(wicketId, rowModel), rowModel);
					}
					@Override
					public MarkupContainer getLink(String wicketId, IModel<T> rowModel) {
						if (linkGeneratorMapper != null) {
							return decorate(linkGeneratorMapper.map(rowModel).link(wicketId));
						}
						return new InvisiblePanel(wicketId);
					}
					@Override
					public MarkupContainer getSideLink(String wicketId, IModel<T> rowModel) {
						if (sideLinkGeneratorMapper != null) {
							return decorate(sideLinkGeneratorMapper.map(rowModel).link(wicketId))
									.add(new WebMarkupContainer("sideLinkIcon").add(new ClassAttributeAppender("fa fa-share-square-o")));
						}
						return new InvisiblePanel(wicketId);
					}
				}.setRenderBodyOnly(true) // Delete useless <div> under <td>. If it could be usefull, give the ability to configure it
		);
	}

	protected abstract CoreLabel newLabel(String componentId, IModel<T> rowModel);

	private CoreLabel decorate(CoreLabel label, IModel<T> rowModel) {
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
		if (tooltipRenderer != null) {
			label.add(new AttributeModifier("title", tooltipRenderer.asModel(rowModel)));
		}
		return label;
	}
	
	private AbstractDynamicBookmarkableLink decorate(AbstractDynamicBookmarkableLink link) {
		if (hideIfInvalid) {
			link.hideIfInvalid();
		}
		for (Behavior linkBehavior : linkBehaviors) {
			link.add(linkBehavior);
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
	
	public Renderer<? super T> getTooltipRenderer() {
		return tooltipRenderer;
	}

	public void setTooltipRenderer(Renderer<? super T> tooltipRenderer) {
		this.tooltipRenderer = tooltipRenderer;
	}

	public IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> getLinkGeneratorMapper() {
		return linkGeneratorMapper;
	}

	public CoreLabelColumn<T, S> setLinkGeneratorMapper(IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> linkGeneratorFactory) {
		if (sideLinkGeneratorMapper != null) {
			throw new IllegalStateException("link and side link cannot be both set.");
		}
		this.linkGeneratorMapper = linkGeneratorFactory;
		return this;
	}

	public IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> getSideLinkGeneratorMapper() {
		return sideLinkGeneratorMapper;
	}

	public CoreLabelColumn<T, S> setSideLinkGeneratorMapper(IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> sideLinkGeneratorFactory) {
		if (linkGeneratorMapper != null) {
			throw new IllegalStateException("link and side link cannot be both set.");
		}
		this.sideLinkGeneratorMapper = sideLinkGeneratorFactory;
		return this;
	}

	public CoreLabelColumn<T, S> hideIfInvalid() {
		this.hideIfInvalid = true;
		return this;
	}

	public void addLinkBehavior(Behavior behavior) {
		linkBehaviors.add(behavior);
	}

}
