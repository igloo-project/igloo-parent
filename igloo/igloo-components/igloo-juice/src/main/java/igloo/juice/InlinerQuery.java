package igloo.juice;

public class InlinerQuery {
	
	private String content;
	
	private InlinerOptions options;
	
	public synchronized String getContent() {
		return content;
	}
	
	public synchronized void setContent(String content) {
		this.content = content;
	}
	
	public synchronized InlinerOptions getOptions() {
		return options;
	}
	
	public synchronized void setOptions(InlinerOptions options) {
		this.options = options;
	}
}