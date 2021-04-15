package org.iglooproject.spring.notification.model;

import java.util.function.Consumer;

public class NotificationContentBody implements INotificationContentBody {

	private static final long serialVersionUID = 1L;

	private String plainText;

	private String htmlText;

	private NotificationContentBody(String plainText, String htmlText) {
		super();
		setPlainText(plainText);
		setHtmlText(htmlText);
	}

	@Override
	public String getPlainText() {
		return plainText;
	}

	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}

	@Override
	public String getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;
	}

	public static NotificationContentBodyBuilder start() {
		return new NotificationContentBodyBuilder();
	}

	public static class NotificationContentBodyBuilder {
		
		private String plainText;
		
		private String htmlText;
		
		public String getPlainText() {
			return plainText;
		}
		
		public void setPlainText(String plainText) {
			this.plainText = plainText;
		}
		
		public String getHtmlText() {
			return htmlText;
		}
		
		public void setHtmlText(String htmlText) {
			this.htmlText = htmlText;
		}
		
		public NotificationContentBodyBuilder with(Consumer<NotificationContentBodyBuilder> builderFunction) {
			builderFunction.accept(this);
			return this;
		}
		
		public NotificationContentBody build() {
			return new NotificationContentBody(plainText, htmlText);
		}
		
	}

}
