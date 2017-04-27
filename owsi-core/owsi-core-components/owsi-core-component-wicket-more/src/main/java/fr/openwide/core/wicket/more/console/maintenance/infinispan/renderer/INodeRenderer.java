package fr.openwide.core.wicket.more.console.maintenance.infinispan.renderer;

import java.util.Locale;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.infinispan.model.INode;
import fr.openwide.core.infinispan.service.IInfinispanClusterService;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRendererInformation;

public class INodeRenderer {

	private static final INodeAnonymousRenderer ANONYMOUS = new INodeAnonymousRenderer();

	private static final INodeStatusRenderer STATUS = new INodeStatusRenderer();
	
	private static final INodeLocalRenderer LOCAL = new INodeLocalRenderer();
	
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
		
		public INodeStatusRenderer() {
			Injector.get().inject(this);
		}
		
		@Override
		protected BootstrapRendererInformation doRender(INode value, Locale locale) {
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
		
		public INodeLocalRenderer() {
			Injector.get().inject(this);
		}
		
		@Override
		protected BootstrapRendererInformation doRender(INode value, Locale locale) {
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

