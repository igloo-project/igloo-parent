package test.autoprefixer;

import org.assertj.core.api.Assertions;
import org.iglooproject.autoprefixer.Autoprefixer;
import org.iglooproject.autoprefixer.AutoprefixerException;
import org.junit.Test;

public class TestAutoprefixer {

	@Test
	public void autoprefixer() throws AutoprefixerException {
		String processed = Autoprefixer.simple().process(
				"@supports (position: sticky) {\n" + 
				"  .sticky-top {\n" + 
				"    position: sticky;\n" + 
				"    top: 0;\n" + 
				"    z-index: 1020;\n" + 
				"  }\n" + 
				"}");
		Assertions.assertThat(processed).isEqualTo(
				"@supports ((position: -webkit-sticky) or (position: sticky)) {\n" + 
				"  .sticky-top {\n" + 
				"    position: -webkit-sticky;\n" + 
				"    position: sticky;\n" + 
				"    top: 0;\n" + 
				"    z-index: 1020;\n" + 
				"  }\n" + 
				"}");
	}

	@Test
	public void syntaxError() {
		Assertions.assertThatExceptionOfType(AutoprefixerException.class)
			.isThrownBy(() -> Autoprefixer.simple().process("css invalid"));
	}

}
