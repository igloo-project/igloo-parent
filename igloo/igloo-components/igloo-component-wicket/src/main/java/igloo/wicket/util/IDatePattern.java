package igloo.wicket.util;

public interface IDatePattern {
	
	String getJavaPatternKey();

	String getJavascriptPatternKey();
	
	boolean capitalize();

}
