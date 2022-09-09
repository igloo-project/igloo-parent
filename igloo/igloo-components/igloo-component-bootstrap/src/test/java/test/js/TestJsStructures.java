package test.js;

import static igloo.bootstrap.jsmodel.JsHelpers.function;
import static igloo.bootstrap.jsmodel.JsHelpers.of;
import static igloo.bootstrap.jsmodel.JsHelpers.ofFunction;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import igloo.bootstrap.js.statement.JsLiteral;
import igloo.bootstrap.js.statement.JsNumber;
import igloo.bootstrap.js.statement.JsString;
import igloo.bootstrap.js.type.JsNumberType;
import igloo.bootstrap.js.type.JsStringType;
import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.bootstrap.popover.JsPopover;

class TestJsStructures {

	@Test
	void testStructures() {
		assertThat(of(true).render()).isEqualTo("true");
		assertThat(of(false).render()).isEqualTo("false");
		assertThat(of((Boolean) null).render()).isEqualTo("null");
		
		assertThat(of(10).render()).isEqualTo("10");
		assertThat(of(10.0).render()).isEqualTo("10.0");
		assertThat(of(10l).render()).isEqualTo("10");
		assertThat(of(-10).render()).isEqualTo("-10");
		assertThat(of(-10l).render()).isEqualTo("-10");
		assertThat(of(-10.0).render()).isEqualTo("-10.0");
		assertThat(of((Number) null).render()).isEqualTo("null");
		
		assertThat(of("simpleValue").render()).isEqualTo("\"simpleValue\"");
		assertThat(of("escaped\"String").render()).isEqualTo("\"escaped\\\"String\"");
		assertThat(of((String) null).render()).isEqualTo("null");
		
		assertThat(ofFunction("myFunction").render()).isEqualTo("myFunction()");
		assertThat(function().functionName("myFunction").build().render()).isEqualTo("myFunction()");
		assertThat(function()
				.functionName("myFunction")
					.addArguments(JsNumber.of(10).anyType())
					.addArguments(JsString.of("marcel").anyType())
					.addArguments(JsLiteral.of("variable").anyType())
				.build().render()).isEqualTo("myFunction(10, \"marcel\", variable)");
	}

	@Test
	void testPopover() {
		assertThat(
			JsPopover.builder()
				.animation(of(true))
				.boundary(of("clippingParents").anyType())
				.container(of("body").anyType())
				.content(of("My content").anyType())
				.customClass(of("class1 class2").anyType())
				.delay(of(10).anyType())
				.fallbackPlacements(JsHelpers.<JsStringType>sequence().addValues(of("bottom"), of("right")).build())
				.html(of(false))
				.offset(JsHelpers.<JsNumberType>sequence().addValues(of(100), of(200)).build().anyType())
				.placement(of("top").anyType())
				.selector(of("[data-bs-toggle=\"popover\"]"))
				.template(of("<dummy template>"))
				.title(of("This is my title").anyType())
				.build()
				.render()
		)
			.isEqualTo("{animation: true, "
				+ "boundary: \"clippingParents\", "
				+ "container: \"body\", "
				+ "content: \"My content\", "
				+ "customClass: \"class1 class2\", "
				+ "delay: 10, "
				+ "fallbackPlacements: [\"bottom\", \"right\"], "
				+ "html: false, "
				+ "offset: [100, 200], "
				+ "placement: \"top\", "
				+ "selector: \"[data-bs-toggle=\\\"popover\\\"]\", "
				+ "template: \"<dummy template>\", "
				+ "title: \"This is my title\"}");
	}

}
