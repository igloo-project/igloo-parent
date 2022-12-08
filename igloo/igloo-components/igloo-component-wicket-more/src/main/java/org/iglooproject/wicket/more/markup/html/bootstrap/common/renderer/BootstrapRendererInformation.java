package org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer;

import java.io.Serializable;
import java.util.Objects;

import igloo.bootstrap.common.BootstrapColor;
import igloo.bootstrap.common.IBootstrapColor;

public final class BootstrapRendererInformation implements Serializable {

	private static final long serialVersionUID = -6016103792390046731L;

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(BootstrapRendererInformation base) {
		return new Builder(base);
	}

	private final String label ;

	private final String iconCssClass;

	private final IBootstrapColor color;

	private final String tooltip;

	private BootstrapRendererInformation(Builder builder) {
		this.label = builder.label;
		this.iconCssClass = builder.iconCssClass;
		this.color = builder.color;
		this.tooltip = builder.tooltip;
	}

	public static final class Builder {
		
		private String label;
		
		private String iconCssClass;
		
		private IBootstrapColor color = BootstrapColor.SECONDARY;
		
		private String tooltip;
		
		private Builder() {
		}
		
		private Builder(BootstrapRendererInformation base) {
			if (base != null) {
				this.label = base.label;
				this.iconCssClass = base.iconCssClass;
				this.color = base.color;
				this.tooltip = base.tooltip;
			}
		}
		
		public Builder label(String label) {
			this.label = label;
			return this;
		}
		
		public Builder icon(String iconCssClass) {
			this.iconCssClass = iconCssClass;
			return this;
		}
		
		public Builder color(IBootstrapColor color) {
			this.color = Objects.requireNonNull(color);
			return this;
		}
		
		public Builder tooltip(String tooltip) {
			this.tooltip = tooltip;
			return this;
		}
		
		public BootstrapRendererInformation build() {
			return new BootstrapRendererInformation(this);
		}
	}

	public String getLabel() {
		return label;
	}

	public String getIconCssClass() {
		return iconCssClass;
	}

	public IBootstrapColor getColor() {
		return color;
	}

	public String getTooltip() {
		return tooltip;
	}

}
