package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.caroufredsel;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

public class CarouFredSel implements ChainableStatement, Serializable {
	private static final long serialVersionUID = 5448191289229125305L;
	
	private Boolean circular;

	private Boolean infinite;

	private CarouFredSelDirection direction;
	
	private Integer width;

	private Integer height;

	private Integer padding;
	
	private Integer itemsVisible;
	
	private Integer scrollDuration;

	private Boolean autoPlay;
	
	private Integer autoDuration;
	
	private Boolean autoPauseOnHover;

	private Component nextButton;
	
	private String nextKey;

	private Component previousButton;
	
	private String previousKey;

	private Component paginationContainer;
	
	@Override
	public String chainLabel() {
		return "carouFredSel";
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		if (circular != null) {
			options.put("circular", circular);
		}
		if (infinite != null) {
			options.put("infinite", infinite);
		}
		if (direction != null) {
			options.put("direction", JsUtils.quotes(direction.getValue()));
		}
		if (width != null) {
			options.put("width", width);
		}
		if (height != null) {
			options.put("height", height);
		}
		if (padding != null) {
			options.put("padding", padding);
		}
		
		// items options
		Options itemsOptions = new Options();
		if (itemsVisible != null) {
			itemsOptions.put("visible", itemsVisible);
		}
		options.put("items", itemsOptions.getJavaScriptOptions().toString());
		
		// scroll options
		Options scrollOptions = new Options();
		if (scrollDuration != null) {
			scrollOptions.put("duration", scrollDuration);
		}
		options.put("scroll", scrollOptions.getJavaScriptOptions().toString());
		
		// auto options
		Options autoOptions = new Options();
		if (autoPlay != null) {
			autoOptions.put("play", autoPlay);
		}
		if (autoDuration != null) {
			autoOptions.put("duration", autoDuration);
		}
		if (autoPauseOnHover != null) {
			autoOptions.put("pauseOnHover", autoPauseOnHover);
		}
		options.put("auto", autoOptions.getJavaScriptOptions().toString());
		
		// prev options
		Options prevOptions = new Options();
		if (previousButton != null) {
			prevOptions.put("button", JsUtils.quotes("#" + previousButton.getMarkupId(), true));
		}
		if (previousKey != null) {
			prevOptions.put("key", JsUtils.quotes(previousKey, true));
		}
		options.put("prev", prevOptions.getJavaScriptOptions().toString());
		
		// next options
		Options nextOptions = new Options();
		if (nextButton != null) {
			nextOptions.put("button", JsUtils.quotes("#" + nextButton.getMarkupId(), true));
		}
		if (nextKey != null) {
			nextOptions.put("key", JsUtils.quotes(nextKey, true));
		}
		options.put("next", nextOptions.getJavaScriptOptions().toString());
		
		// pagination options
		Options paginationOptions = new Options();
		if (paginationContainer != null) {
			paginationOptions.put("container", JsUtils.quotes("#" + paginationContainer.getMarkupId(), true));
		}
		options.put("pagination", paginationOptions.getJavaScriptOptions().toString());
		
		CharSequence[] args = new CharSequence[1];
		args[0] = options.getJavaScriptOptions();
		return args;
	}
	
	public Integer getScrollDuration() {
		return scrollDuration;
	}

	public void setScrollDuration(Integer scrollDuration) {
		this.scrollDuration = scrollDuration;
	}

	public Boolean getAutoPlay() {
		return autoPlay;
	}

	public void setAutoPlay(Boolean autoPlay) {
		this.autoPlay = autoPlay;
	}
	
	public Integer getAutoDuration() {
		return autoDuration;
	}

	public void setAutoDuration(Integer autoDuration) {
		this.autoDuration = autoDuration;
	}

	public Boolean getAutoPauseOnHover() {
		return autoPauseOnHover;
	}

	public void setAutoPauseOnHover(Boolean autoPauseOnHover) {
		this.autoPauseOnHover = autoPauseOnHover;
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

	public Component getNextButton() {
		return nextButton;
	}

	public void setNextButton(Component nextButton) {
		this.nextButton = nextButton;
	}
	
	public String getNextKey() {
		return nextKey;
	}

	public void setNextKey(String nextKey) {
		this.nextKey = nextKey;
	}

	public Component getPreviousButton() {
		return previousButton;
	}

	public void setPreviousButton(Component previousButton) {
		this.previousButton = previousButton;
	}
	
	public String getPreviousKey() {
		return previousKey;
	}

	public void setPreviousKey(String previousKey) {
		this.previousKey = previousKey;
	}

	public Component getPaginationContainer() {
		return paginationContainer;
	}

	public void setPaginationContainer(Component paginationContainer) {
		this.paginationContainer = paginationContainer;
	}

	public Integer getItemsVisible() {
		return itemsVisible;
	}

	public void setItemsVisible(Integer itemsVisible) {
		this.itemsVisible = itemsVisible;
	}

	public CarouFredSelDirection getDirection() {
		return direction;
	}

	public void setDirection(CarouFredSelDirection direction) {
		this.direction = direction;
	}

}
