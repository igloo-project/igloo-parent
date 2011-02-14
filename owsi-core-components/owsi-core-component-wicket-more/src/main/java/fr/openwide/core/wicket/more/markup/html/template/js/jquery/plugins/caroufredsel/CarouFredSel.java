package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.caroufredsel;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

public class CarouFredSel implements ChainableStatement, Serializable {
	private static final long serialVersionUID = 5448191289229125305L;
	
	private Boolean auto;
	
	private Integer width;
	
	private Integer height;
	
	private Integer padding;
	
	private Boolean circular;
	
	private Boolean infinite;
	
	private Component next;
	
	private Component previous;
	
	private Component pagination;
	
	private Integer items;

	@Override
	public String chainLabel() {
		return "carouFredSel";
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		if (width != null) {
			options.put("width", width);
		}
		if (height != null) {
			options.put("height", height);
		}
		if (padding != null) {
			options.put("padding", padding);
		}
		if (circular != null) {
			options.put("circular", circular);
		}
		if (infinite != null) {
			options.put("infinite", infinite);
		}
		if (items != null) {
			options.put("items", items);
		}
		if (previous != null) {
			options.put("prev", JsUtils.quotes("#" + previous.getMarkupId(), true));
		}
		if (next != null) {
			options.put("next", JsUtils.quotes("#" + next.getMarkupId(), true));
		}
		if (pagination != null) {
			options.put("pagination", JsUtils.quotes("#" + pagination.getMarkupId(), true));
		}
		if (auto != null) {
			options.put("auto", auto);
		}
		CharSequence[] args = new CharSequence[1];
		args[0] = options.getJavaScriptOptions();
		return args;
	}

	public Boolean getAuto() {
		return auto;
	}

	public void setAuto(Boolean auto) {
		this.auto = auto;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getPadding() {
		return padding;
	}

	public void setPadding(Integer padding) {
		this.padding = padding;
	}

	public Boolean getCircular() {
		return circular;
	}

	public void setCircular(Boolean circular) {
		this.circular = circular;
	}

	public Boolean getInfinite() {
		return infinite;
	}

	public void setInfinite(Boolean infinite) {
		this.infinite = infinite;
	}

	public Component getNext() {
		return next;
	}

	public void setNext(Component next) {
		this.next = next;
	}

	public Component getPrevious() {
		return previous;
	}

	public void setPrevious(Component previous) {
		this.previous = previous;
	}

	public Component getPagination() {
		return pagination;
	}

	public void setPagination(Component pagination) {
		this.pagination = pagination;
	}

	public Integer getItems() {
		return items;
	}

	public void setItems(Integer items) {
		this.items = items;
	}

}
