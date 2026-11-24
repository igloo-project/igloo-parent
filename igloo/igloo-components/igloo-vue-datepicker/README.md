# Igloo Vue datepicker

Wicket form fields for dates and times, edited with [VueDatePicker](https://vue3datepicker.com/).

You write Java only: the field renders the datepicker, submits its value with the form and converts
it to a `java.time` value, like a `TextField` would. Vue and the JavaScript are internal details.

## Setup

**Dependency.**

```xml
<dependency>
  <groupId>org.iglooproject.components</groupId>
  <artifactId>igloo-vue-datepicker</artifactId>
  <version>${igloo.version}</version>
</dependency>
```

**Styles.** Import the Bootstrap theme in your SCSS:

```scss
@import "META-INF/resources/webjars/igloo-vue-datepicker/scss/vue-datepicker";
```

and add the VueDatePicker stylesheet as a dependency of your styles resource reference:

```java
@Override
public List<HeaderItem> getDependencies() {
  List<HeaderItem> dependencies = super.getDependencies();
  dependencies.add(CssHeaderItem.forReference(VueDatePickerCssResourceReference.get()));
  return dependencies;
}
```

**Resource keys.** The fields read their formats and labels from your application resources. The
basic application provides them all, in English and French:

| Keys | Usage |
|---|---|
| `datetime.pattern.{date,dateTime,time,yearMonth,year}` | Format displayed and parsed, a `DateTimeFormatter` pattern |
| `datetime.pattern.*.placeholder`, `datetime.pattern.{date,dateTime}.range.placeholder` | Placeholders |
| `datepicker.wording.{select,cancel}` | Buttons |
| `datepicker.aria.label.*` | Accessibility labels |
| `conversion.error.{localDate,localDateTime,localTime,yearMonth,year}`, `conversion.error.{localDate,localDateTime}.range` | Error when the input cannot be parsed |

## Usage

In the HTML, use a `vue-date-picker` tag:

```html
<label wicket:for="birthDate"><wicket:message key="person.birthDate" /></label>
<vue-date-picker wicket:id="birthDate"></vue-date-picker>
```

In Java, add the field matching the type of your model:

```java
form.add(
    new LocalDatePickerVueField("birthDate", BindingModel.of(personModel, Bindings.person().birthDate()))
        .setRequired(true)
        .setLabel(new ResourceModel("person.birthDate")));
```

| Field | Model | Picks |
|---|---|---|
| `LocalDatePickerVueField` | `IModel<LocalDate>` | a date |
| `LocalDateTimePickerVueField` | `IModel<LocalDateTime>` | a date and a time |
| `LocalTimePickerVueField` | `IModel<LocalTime>` | a time |
| `YearMonthPickerVueField` | `IModel<YearMonth>` | a month |
| `YearPickerVueField` | `IModel<Year>` | a year |
| `LocalDateRangePickerVueField` | two `IModel<LocalDate>`, start and end | a date range |
| `LocalDateTimeRangePickerVueField` | two `IModel<LocalDateTime>`, start and end | a date and time range |

The fields are regular `FormComponent`s: validators, `setRequired`, labels with `wicket:for`, Ajax
behaviors and form submission work as usual. The input is parsed with the `datetime.pattern.*`
format of the field, whatever the session locale.

A field disabled with `setEnabled(false)`, or inside a disabled container, renders a disabled
datepicker: Wicket would ignore its input anyway.

## Options

The datepicker accepts many [options](https://vue3datepicker.com/props/modes/). Each field sets
defaults; you change them with a builder, either in the constructor or with `addToBuilder`:

```java
// in the constructor
new LocalDatePickerVueField("appointment", model, builder -> builder.autoApply(JsHelpers.of(false)));

// after construction, e.g. in a subclass
new LocalDatePickerVueField("appointment", model)
    .addToBuilder(builder -> builder.filters(JsHelpers.ofLiteral("{ weekDays: [6, 0] }")));
```

Options are applied in this order, the last one wins: field defaults, constructor, `addToBuilder`
calls. Set an option to `null` to remove a default, e.g. `builder.textInput(null)` to disable typing
in the input.

Option values are JavaScript code, written in the page as is. Build them with `JsHelpers`:

| Helper | Renders | Example |
|---|---|---|
| `JsHelpers.of(true)`, `JsHelpers.of(3)` | a boolean, a number | `builder.multiCalendars(JsHelpers.of(true))` |
| `JsHelpers.of("dd/MM/yyyy")` | an escaped string | `builder.format(JsHelpers.of("MM/yyyy"))` |
| `JsHelpers.ofLocalDateModel(model)` | the date of a model | `builder.maxDate(JsHelpers.ofLocalDateModel(Model.of(LocalDate.now())))` |
| `JsHelpers.sequence()` | an array | `builder.disabledDates(JsHelpers.sequence().values(dates).build())` |
| `JsHelpers.ofLiteral("...")` | JavaScript code | `builder.markers(JsHelpers.ofLiteral("[{ date: new Date(), type: 'dot' }]"))` |

Common shortcuts are available on the fields: `minDate(IModel<LocalDate>)`, `maxDate(...)` and
`labelPlaceholder()`, which uses the field label as placeholder.

The input is parsed with the `format` option, the pattern the datepicker displays the value with:
set it with `JsHelpers.of(String)` or `JsHelpers.ofString(IModel<String>)`, and use letters that
mean the same for [date-fns](https://date-fns.org/docs/format) and `DateTimeFormatter`, e.g. `dd`,
`MM`, `yyyy`, `HH`, `mm`. A format given as a JavaScript function cannot parse: override
`getFormat()` in the field.

The classes of the input, option `ui`, accept the forms of a Vue class binding: a string, an array,
or an object whose keys are the classes, enabled by a true value, e.g. `{ input: { 'form-control': true } }`.

## Linking two datepickers

The min or max date of a datepicker can follow the value of another one, in the browser, without
any server round trip:

```java
LocalDatePickerVueField start = new LocalDatePickerVueField("start", startModel);
LocalDatePickerVueField end = new LocalDatePickerVueField("end", endModel);
start.maxDate(end);
end.minDate(start);
```

The link survives Ajax refreshes of either field.

## Reacting to changes

**On the server**, add an Ajax behavior, as for any field. The new value is in the model when the
behavior runs:

```java
field.add(new UpdateOnChangeAjaxEventBehavior().onChange(...));
```

**In the browser**, give a JavaScript function to `onUpdateModel`. It receives the value picked by the
user. `getJsSetModelValue` writes the value of another datepicker:

```java
first.addToBuilder(
    builder -> builder.onUpdateModel(JsHelpers.ofLiteral("value => " + second.getJsSetModelValue("value"))));
```

A value set this way is not sent to the server immediately, but with the form. `getJsModelValue`
reads the value of a datepicker, for your own options.

## Validation feedback

The fields implement `IVueComponent`: `FormInvalidDecoratorListener` adds and removes the `is-invalid`
class on the datepicker input after each Ajax submission, as for other fields.

## Locales

Day and month names use the language of the Wicket session. French and English (US) are bundled.
Other languages fall back to English (US).

To support another language, or to use British English, register a
[date-fns locale](https://date-fns.org/docs/I18n) from a script of your project:

```js
import { de } from 'date-fns/locale/de';
import { enGB } from 'date-fns/locale/en-GB';

vueInit.registerLocale(de);          // for German sessions
vueInit.registerLocale(enGB, 'en');  // British English instead of US English
```

Load this script after the datepicker one, with a resource reference depending on it:

```java
@Override
public List<HeaderItem> getDependencies() {
  return List.of(JavaScriptHeaderItem.forReference(VueInitAppResourceReference.get()));
}
```

## How it works

1. Wicket renders the `vue-date-picker` tag empty, with its markup id. This tag is the host of the
   datepicker and always stays in the page.
2. On DOM ready, a script calls `vueInit.mount(markupId, config)`. The configuration holds the input
   name, the session language, the value of the model and the options.
3. Vue renders the datepicker inside the host. Its input bears the Wicket input name: the value is
   submitted with the form and converted by the field.
4. When the user picks a value, a `change` event is fired on the input, once it shows the new value.
   This is the event Wicket Ajax behaviors listen to.
5. When the field leaves the page, an observer of the page unmounts the datepicker: otherwise it
   would stay alive in memory, with its calendar left in the page. It works whoever removed the
   field: a Wicket Ajax response replacing the field or one of its parents, Bootstrap destroying a
   modal, a project script. A field moved elsewhere in the page stays mounted.
6. When an Ajax response refreshes the field, the script of the new markup mounts the datepicker
   again, after unmounting the previous one if the observer has not done it yet.

The Java side lives in `VueDatePickerBehavior`, which renders the script, and
`AbstractDatePickerVueField`, the base class of the fields. The JavaScript side is
`src/main/js/vueInit.ts`, bundled as the global `vueInit` with its public functions: `mount`,
`unmount`, `getModelValue`, `setModelValue`, `registerLocale`, `appendInputCssClass` and
`removeInputCssClass`.

## Migrating from previous versions

| Before | Now |
|---|---|
| `getVModel()`, used in `onUpdateModel` as `"value => " + other.getVModel() + ".value = value"` | `"value => " + other.getJsSetModelValue("value")` |
| `getVModelVarName()`, `getVueOnChangeVarName()` | Removed. Use `getJsModelValue()` to read a value |
| Builder options `vif` and `velse` | Removed: show or hide the field with Wicket visibility |
| Day and month names in every language supported by date-fns | Only French and English (US) are bundled. Other languages need `registerLocale`, see [Locales](#locales) |
| `IJsVueDatePicker.values()` keys such as `:min-date` | VueDatePicker prop names, such as `minDate` |
| JavaScript functions of `vueInit` other than the ones listed above | Removed |
| `VueJavaScriptResourceReference` loaded the full Vue build | It loads the runtime build, without the template compiler. Vue templates written in HTML no longer compile with it |
| A model holding a value, or a typed model such as `BindingModel`, made Wicket parse the input with its own converter | The input is always parsed with the field format |
| A `format` option other than a string failed with an unclear error on submission | Override `getFormat()`, see [Options](#options) |

## Development

The JavaScript is built by Maven, through `frontend-maven-plugin`, into
`src/main/generated-js/dist`. During development, from this directory:

```shell
npm ci       # installs the versions locked in package-lock.json
npm run js   # builds vueInit.js and vueInit.css
npm test     # JavaScript tests, Vitest
```

npm dependencies are locked in `package-lock.json`, versioned so that every build bundles the same
code. Maven installs them with `npm ci`, which fails if the lock file does not match
`package.json`: after changing a dependency, run `npm install` and commit both files.

Maven runs both test suites in the `test` phase: Java tests first, then `npm test`. A failing
JavaScript test fails the build, before packaging. `-DskipTests` skips both suites and
`-Dmaven.test.failure.ignore` applies to both; `-Dmaven.test.skip` does not skip the JavaScript tests.

Both test suites describe the expected behaviour:

- `src/test/js/datePicker.spec.ts` runs the datepicker in a simulated browser, jsdom, and replays
  what Wicket does: page rendering, Ajax replacements, user changes. Only
  `src/test/js/support/datePickerTester.ts` knows the `vueInit` API.
- `src/test/java` renders the fields with `WicketTester`: options, generated script, conversion of
  the submitted input.
