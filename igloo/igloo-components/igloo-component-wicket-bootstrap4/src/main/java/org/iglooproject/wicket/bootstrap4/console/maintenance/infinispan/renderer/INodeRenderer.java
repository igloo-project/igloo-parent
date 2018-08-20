package org.iglooproject.wicket.bootstrap4.console.maintenance.infinispan.renderer;

import static org.iglooproject.spring.util.StringUtils.emptyTextToNull;

import java.util.Locale;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.Joiners;
import org.iglooproject.infinispan.model.INode;
import org.iglooproject.infinispan.service.IInfinispanClusterService;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.model.BootstrapColor;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation;
import org.iglooproject.wicket.more.rendering.Renderer;

public class INodeRenderer {

	private static final Renderer<INode> INSTANCE = new Renderer<INode>() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(INode value, Locale locale) {
			if (value == null) {
				return null;
			}
			return Joiners.onMiddotSpace()
					.join(
							emptyTextToNull(value.getAddress().toString()),
							emptyTextToNull(value.getName())
					);
		}
	};

	private static final INodeAnonymousRenderer ANONYMOUS = new INodeAnonymousRenderer();

	private static final INodeStatusRenderer STATUS = new INodeStatusRenderer();

	private static final INodeLocalRenderer LOCAL = new INodeLocalRenderer();

	public static Renderer<INode> get() {
		return INSTANCE;
	}

	public static INodeAnonymousRenderer anonymous() {
		return ANONYMOUS;
	}

	public static INodeStatusRenderer status() {
		return STATUS;
	}

	public static INodeLocalRenderer local(){
		return LOCAL;
	}

	private static class INodeAnonymousRenderer extends BootstrapRenderer<INode> {
		private static final long serialVersionUID = 1L;
		
		@Override
		protected BootstrapRendererInformation doRender(INode value, Locale locale) {
			if (value == null) {
				return BootstrapRendererInformation.builder().build();
			}
			
			if (!value.isAnonymous()) {
				return BootstrapRendererInformation.builder().build();
			}
			
			return BootstrapRendererInformation.builder()
					.icon("fa fa-exclamation")
					.color(BootstrapColor.DANGER)
					.label(getString("business.infinispan.node.anonymous", locale))
					.build();
		}
	}

	private static class INodeStatusRenderer extends BootstrapRenderer<INode> {
		private static final long serialVersionUID = 1L;
		
		@SpringBean
		private IInfinispanClusterService infinispanClusterService;
		
		private boolean initialized;
		
		public INodeStatusRenderer() {
		}
		
		@Override
		protected BootstrapRendererInformation doRender(INode value, Locale locale) {
			if (!initialized) {
				Injector.get().inject(this);
				initialized = true;
			}
			
			if (value == null) {
				return BootstrapRendererInformation.builder().build();
			}
			
			if (infinispanClusterService.getMembers().contains(value.getAddress())) {
				return BootstrapRendererInformation.builder()
						.icon("fa fa-toggle-on")
						.label(getString("business.infinispan.node.connected", locale))
						.build();
			}
			
			return BootstrapRendererInformation.builder()
					.icon("fa fa-toggle-off")
					.label(getString("business.infinispan.node.disconnected", locale))
					.build();
		}
		
	}

	private static class INodeLocalRenderer extends BootstrapRenderer<INode> {
		private static final long serialVersionUID = 1L;
		
		@SpringBean
		private IInfinispanClusterService infinispanClusterService;
		
		private boolean initialized;
		
		public INodeLocalRenderer() {
		}
		
		@Override
		protected BootstrapRendererInformation doRender(INode value, Locale locale) {
			if (!initialized) {
				Injector.get().inject(this);
				initialized = true;
			}
			
			if (infinispanClusterService.getLocalAddress().equals(value.getAddress())) {
				return BootstrapRendererInformation.builder()
						.icon("fa fa-user")
						.label(getString("business.infinispan.node.local", locale))
						.build();
			}
			
			return BootstrapRendererInformation.builder().build();
		}
	}

}

