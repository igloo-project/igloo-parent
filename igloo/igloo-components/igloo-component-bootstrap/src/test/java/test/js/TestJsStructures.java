package test.js;

import static igloo.bootstrap.jsmodel.JsHelpers.function;
import static igloo.bootstrap.jsmodel.JsHelpers.of;
import static igloo.bootstrap.jsmodel.JsHelpers.ofFunction;
import static org.assertj.core.api.Assertions.assertThat;

import igloo.bootstrap.js.statement.JsLiteral;
import igloo.bootstrap.js.statement.JsNumber;
import igloo.bootstrap.js.statement.JsString;
import igloo.bootstrap.js.type.JsNumberType;
import igloo.bootstrap.js.type.JsStringType;
import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.bootstrap.popover.JsPopover;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.apache.wicket.model.Model;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

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
    assertThat(of("back\\slash").render()).isEqualTo("\"back\\\\slash\"");
    assertThat(of("line\nbreak\r\ttab").render()).isEqualTo("\"line\\nbreak\\r\\ttab\"");
    assertThat(of("</script><!-- ]]>").render())
        .isEqualTo("\"\\u003C/script\\u003E\\u003C!-- ]]\\u003E\"");
    assertThat(of("\u0000\u2028\u2029").render()).isEqualTo("\"\\u0000\\u2028\\u2029\"");
    assertThat(of("àé€😀").render()).isEqualTo("\"àé€😀\"");
    assertThat(of((String) null).render()).isEqualTo("null");

    LocalDate start = LocalDate.of(2026, 1, 15);
    LocalDate end = LocalDate.of(2026, 1, 20);
    assertThat(JsHelpers.ofLocalDatePair(Model.of(Pair.with(start, end))).render())
        .isEqualTo("[%d,%d]".formatted(epochMilli(start), epochMilli(end)));
    assertThat(JsHelpers.ofLocalDatePair(Model.of(Pair.<LocalDate, LocalDate>with(start, null))).render()).isEqualTo("[]");
    assertThat(JsHelpers.ofLocalDatePair(Model.of(Pair.<LocalDate, LocalDate>with(null, end))).render()).isEqualTo("[]");
    assertThat(JsHelpers.ofLocalDatePair(Model.of()).render()).isEqualTo("[]");
    assertThat(
            JsHelpers.ofLocalDateTimePair(
                    Model.of(Pair.<LocalDateTime, LocalDateTime>with(null, end.atStartOfDay())))
                .render())
        .isEqualTo("[]");

    assertThat(ofFunction("myFunction").render()).isEqualTo("myFunction()");
    assertThat(function().functionName("myFunction").build().render()).isEqualTo("myFunction()");
    assertThat(
            function()
                .functionName("myFunction")
                .addArguments(JsNumber.of(10))
                .addArguments(JsString.of("marcel"))
                .addArguments(JsLiteral.of("variable"))
                .build()
                .render())
        .isEqualTo("myFunction(10, \"marcel\", variable)");
  }

  @Test
  void testPopover() {
    assertThat(
            JsPopover.builder()
                .animation(of(true))
                .boundary(of("clippingParents"))
                .container(of("body"))
                .content(of("My content"))
                .customClass(of("class1 class2"))
                .delay(of(10))
                .fallbackPlacements(
                    JsHelpers.<JsStringType>sequence().addValues(of("bottom"), of("right")).build())
                .html(of(false))
                .offset(JsHelpers.<JsNumberType>sequence().addValues(of(100), of(200)).build())
                .placement(of("top"))
                .selector(of("[data-bs-toggle=\"popover\"]"))
                .template(of("<dummy template>"))
                .title(of("This is my title"))
                .build()
                .render())
        .isEqualTo(
            "{animation: true, "
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
                + "template: \"\\u003Cdummy template\\u003E\", "
                + "title: \"This is my title\"}");
  }

  private static long epochMilli(LocalDate date) {
    return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }
}
