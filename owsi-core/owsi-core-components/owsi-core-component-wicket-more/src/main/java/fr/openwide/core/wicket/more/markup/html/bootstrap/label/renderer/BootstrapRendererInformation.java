package fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer;

import java.io.Serializable;
import java.util.Objects;

import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.IBootstrapColor;

public class BootstrapRendererInformation implements Serializable {

	private static final long serialVersionUID = -6016103792390046731L;

	private final String label ;

	private final String iconCssClass;

	private final IBootstrapColor color;

	private final String tooltip;

	private BootstrapRendererInformation(Builder builder) {
		this.label = builder.getLabel();
		this.iconCssClass = builder.getIconCssClass();
		this.color = builder.getColor();
		this.tooltip = builder.getTooltip();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		
		private String label;
		
		private String iconCssClass;
		
		private IBootstrapColor color = BootstrapColor.DEFAULT;
		
		private String tooltip;
		
		public Builder label(String label) {
			this.label = label;
			return this;
		}
		
		public String getLabel() {
			return label;
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
		
		public Builder tooltip(String tooltip) {
			this.tooltip = tooltip;
			return this;
		}
		
		public String getTooltip() {
			return tooltip;
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
