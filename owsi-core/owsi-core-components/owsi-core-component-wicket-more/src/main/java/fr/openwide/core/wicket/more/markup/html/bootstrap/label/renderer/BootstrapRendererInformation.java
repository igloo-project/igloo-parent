package fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer;

import java.io.Serializable;
import java.util.Objects;

import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.IBootstrapColor;
import fr.openwide.core.wicket.more.rendering.Renderer;

public class BootstrapRendererInformation implements Serializable {

	private static final long serialVersionUID = -6016103792390046731L;

	private final Renderer<Object> labelRenderer ;

	private final String iconCssClass;

	private final IBootstrapColor color;

	private final Renderer<Object> tooltipRenderer;

	private BootstrapRendererInformation(Builder builder) {
		this.labelRenderer = builder.getLabelRenderer();
		this.iconCssClass = builder.getIconCssClass();
		this.color = builder.getColor();
		this.tooltipRenderer = builder.getTooltipRenderer();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		
		private Renderer<Object> labelRenderer = Renderer.constant(null);
		
		private String iconCssClass;
		
		private IBootstrapColor color = BootstrapColor.DEFAULT;
		
		private Renderer<Object> tooltipRenderer = Renderer.constant(null);
		
		public Builder label(String resourceKey) {
			return label(Renderer.fromResourceKey(resourceKey));
		}
		
		public Builder label(Renderer<Object> labelRenderer) {
			this.labelRenderer = Objects.requireNonNull(labelRenderer);
			return this;
		}
		
		private Renderer<Object> getLabelRenderer() {
			return labelRenderer;
		}
		
		public Builder icon(String iconCssClass) {
			this.iconCssClass = iconCssClass;
			return this;
		}
		
		private String getIconCssClass() {
			return iconCssClass;
		}
		
		public Builder color(IBootstrapColor color) {
			this.color = Objects.requireNonNull(color);
			return this;
		}
		
		private IBootstrapColor getColor() {
			return color;
		}
		
		public Builder tooltip(String resourceKey) {
			return tooltip(Renderer.fromResourceKey(resourceKey));
		}
		
		public Builder tooltip(Renderer<Object> tooltipRenderer) {
			this.tooltipRenderer = Objects.requireNonNull(tooltipRenderer);
			return this;
		}
		
		private Renderer<Object> getTooltipRenderer() {
			return tooltipRenderer;
		}
		
		public BootstrapRendererInformation build() {
			return new BootstrapRendererInformation(this);
		}
	}

	public Renderer<Object> getLabelRenderer() {
		return labelRenderer;
	}

	public String getIconCssClass() {
		return iconCssClass;
	}

	public IBootstrapColor getColor() {
		return color;
	}

	public Renderer<Object> getTooltipRenderer() {
		return tooltipRenderer;
	}

}
