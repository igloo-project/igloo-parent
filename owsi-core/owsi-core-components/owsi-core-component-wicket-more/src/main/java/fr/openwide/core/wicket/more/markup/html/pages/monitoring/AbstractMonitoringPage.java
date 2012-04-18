package fr.openwide.core.wicket.more.markup.html.pages.monitoring;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.markup.MarkupType;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

public abstract class AbstractMonitoringPage extends Page {
	
	private static final long serialVersionUID = -2327468306332270500L;

	public static final String TEXT_MIME = "text/plain";

	public static final MarkupType TEXT_PLAIN_MARKUP_TYPE = new MarkupType("txt", TEXT_MIME);
	
	private boolean success;

	private List<String> details = new ArrayList<String>();

	private String message;

	public AbstractMonitoringPage() {
		super();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		add(new Label("status", isSuccess() ? "OK" : "KO").setEscapeModelStrings(false));
		
		add(new ListView<String>("details", getDetails()) {

			private static final long serialVersionUID = 1998240269123369862L;

			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(new Label("detail", item.getModelObject().replaceAll("\\|", "<pipe>")).setEscapeModelStrings(false));
				MarkupContainer separator = new WebMarkupContainer("separator");
				separator.setVisible(item.getIndex() == getList().size() - 1);
				item.add(separator);
			}
		});
		
		add(new Label("message", getMessage()).setEscapeModelStrings(false));
	}

	@Override
	public MarkupType getMarkupType() {
		return TEXT_PLAIN_MARKUP_TYPE;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}