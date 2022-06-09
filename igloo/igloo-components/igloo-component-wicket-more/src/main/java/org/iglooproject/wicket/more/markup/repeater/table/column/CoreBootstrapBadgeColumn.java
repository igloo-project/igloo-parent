package org.iglooproject.wicket.more.markup.repeater.table.column;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;

import com.google.common.collect.Lists;

import igloo.bootstrap.BootstrapRequestCycle;
import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;
import igloo.wicket.markup.html.panel.InvisiblePanel;
import igloo.wicket.model.ReadOnlyModel;

public class CoreBootstrapBadgeColumn<T, S extends ISort<?>, C> extends AbstractCoreColumn<T, S> {

	private static final long serialVersionUID = -5344972073351010752L;

	private final IDetachableFactory<IModel<T>, ? extends IModel<C>> modelFactory;

	private final IBootstrapRenderer<? super C> renderer;
	
	private Condition badgePill = Condition.alwaysFalse();
	
	private Condition showIcon = Condition.alwaysTrue();
	
	private Condition showLabel = Condition.alwaysTrue();
	
	private Condition showTooltip = Condition.alwaysTrue();
	
	private ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> linkGeneratorMapper;
	
	private ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> sideLinkGeneratorMapper;
	
	private enum LinkBehaviorIfInvalid {
		THROW_EXCEPTION,
		HIDE;
	}
	
	private LinkBehaviorIfInvalid linkBehaviorIfInvalid = null;
	
	private List<Behavior> linkBehaviors = Lists.newArrayList();

	public CoreBootstrapBadgeColumn(IModel<?> headerLabelModel, final SerializableFunction2<? super T, C> function,
			final IBootstrapRenderer<? super C> renderer) {
		super(headerLabelModel);
		Injector.get().inject(this);
		
		this.modelFactory = ReadOnlyModel.factory(function);
		this.renderer = renderer;
	}
	
	@Override
	public void detach() {
		super.detach();
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
				new CoreBootstrapBadgeLinkColumnPanel<T, ISort<?>, C>(componentId, rowModel) {
					private static final long serialVersionUID = 1L;
					
					@Override
					public Component getBootstrapBadge(String wicketId, IModel<T> rowModel) {
						return BootstrapRequestCycle.getSettings().badgeSupplier(wicketId, modelFactory.create(rowModel), renderer).get()
								.badgePill(badgePill)
								.showIcon(showIcon)
								.showLabel(showLabel)
								.showTooltip(showTooltip);
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
									.add(new WebMarkupContainer("sideLinkIcon").add(new ClassAttributeAppender("fa fa-fw fa-share-square-o")));
						}
						return new InvisiblePanel(wicketId);
					}
				}.setRenderBodyOnly(true) // Delete useless <div> under <td>. If it could be usefull, give the ability to configure it
		);
	}
	
	private AbstractDynamicBookmarkableLink decorate(AbstractDynamicBookmarkableLink link) {
		if (linkBehaviorIfInvalid != null) {
			switch (linkBehaviorIfInvalid) {
			case HIDE:
				link.hideIfInvalid();
				break;
			case THROW_EXCEPTION:
				link.throwExceptionIfInvalid();
				break;
			}
		}
		for (Behavior linkBehavior : linkBehaviors) {
			link.add(linkBehavior);
		}
		return link;
	}

	public void badgePill(Condition badgePill) {
		this.badgePill = badgePill;
	}

	public void showIcon(Condition showIcon) {
		this.showIcon = showIcon;
	}

	public void showLabel(Condition showLabel) {
		this.showLabel = showLabel;
	}

	public void showTooltip(Condition showTooltip) {
		this.showTooltip = showTooltip;
	}

	public ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> getLinkGeneratorMapper() {
		return linkGeneratorMapper;
	}

	public CoreBootstrapBadgeColumn<T, S, C> setLinkGeneratorMapper(ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> linkGeneratorFactory) {
		if (sideLinkGeneratorMapper != null) {
			throw new IllegalStateException("link and side link cannot be both set.");
		}
		this.linkGeneratorMapper = linkGeneratorFactory;
		return this;
	}

	public ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> getSideLinkGeneratorMapper() {
		return sideLinkGeneratorMapper;
	}

	public CoreBootstrapBadgeColumn<T, S, C> setSideLinkGeneratorMapper(ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> sideLinkGeneratorFactory) {
		if (linkGeneratorMapper != null) {
			throw new IllegalStateException("link and side link cannot be both set.");
		}
		this.sideLinkGeneratorMapper = sideLinkGeneratorFactory;
		return this;
	}

	public CoreBootstrapBadgeColumn<T, S, C> throwExceptionIfInvalid() {
		this.linkBehaviorIfInvalid = LinkBehaviorIfInvalid.THROW_EXCEPTION;
		return this;
	}

	public CoreBootstrapBadgeColumn<T, S, C> hideIfInvalid() {
		this.linkBehaviorIfInvalid = LinkBehaviorIfInvalid.HIDE;
		return this;
	}

	public void addLinkBehavior(Behavior behavior) {
		linkBehaviors.add(behavior);
	}

}
