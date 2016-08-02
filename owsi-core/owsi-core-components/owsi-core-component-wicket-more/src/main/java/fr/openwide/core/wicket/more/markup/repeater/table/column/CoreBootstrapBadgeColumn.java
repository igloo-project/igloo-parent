package fr.openwide.core.wicket.more.markup.repeater.table.column;

import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.binding.AbstractCoreBinding;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.panel.InvisiblePanel;
import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.component.BootstrapBadge;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;

public class CoreBootstrapBadgeColumn<T, S extends ISort<?>, C> extends AbstractCoreColumn<T, S> {

	private static final long serialVersionUID = -5344972073351010752L;

	private final IDetachableFactory<IModel<T>, ? extends IModel<C>> modelFactory;

	private final BootstrapRenderer<? super C> renderer;
	
	private ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> linkGeneratorMapper;
	
	private ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> sideLinkGeneratorMapper;
	
	private enum LinkBehaviorIfInvalid {
		THROW_EXCEPTION,
		HIDE;
	}
	
	private LinkBehaviorIfInvalid linkBehaviorIfInvalid = null;
	
	private List<Behavior> linkBehaviors = Lists.newArrayList();

	public CoreBootstrapBadgeColumn(IModel<?> headerLabelModel, final AbstractCoreBinding<? super T, C> binding,
			final BootstrapRenderer<? super C> renderer) {
		super(headerLabelModel);
		this.modelFactory = BindingModel.factory(binding);
		this.renderer = renderer;
	}

	public CoreBootstrapBadgeColumn(IModel<?> headerLabelModel, final Function<? super T, C> function,
			final BootstrapRenderer<? super C> renderer) {
		super(headerLabelModel);
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
					public BootstrapBadge<C> getBootstrapBadge(String wicketId, IModel<T> rowModel) {
						return new BootstrapBadge<>(wicketId, modelFactory.create(rowModel), renderer);
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
