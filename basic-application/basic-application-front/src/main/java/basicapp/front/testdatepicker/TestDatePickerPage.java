package basicapp.front.testdatepicker;

import basicapp.back.business.common.IDatePickerService;
import basicapp.front.common.template.MainTemplate;
import igloo.bootstrap.js.statement.JsLiteral;
import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.Component.DatePickerRangeVueField;
import igloo.vuedatepicker.Component.DatePickerVueField;
import igloo.vuedatepicker.Component.DateTimePickerVueField;
import igloo.vuedatepicker.Component.MonthYearPickerVueField;
import igloo.vuedatepicker.Component.TimePickerVueField;
import igloo.vuedatepicker.Component.YearPickerVueField;
import igloo.wicket.feedback.FeedbackUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class TestDatePickerPage extends MainTemplate {

  private static final long serialVersionUID = 1L;

  @SpringBean private IDatePickerService datePickerService;

  public TestDatePickerPage(PageParameters parameters) {
    super(parameters);
    Form<Date> form = new Form<>("form");
    IModel<LocalDate> dateClassiqueModel = Model.of();
    IModel<LocalDate> dateSansSaisieInput = Model.of();
    IModel<LocalDate> dateAutoApplyModel = Model.of();
    IModel<LocalDate> datepickerIntervalleFormulaireDebutModel = Model.of();
    IModel<LocalDate> datepickerIntervalleFormulaireFinModel = Model.of();
    IModel<LocalDate> dateRange1Model = Model.of();
    IModel<LocalDate> dateRange2Model = Model.of();
    IModel<LocalDateTime> dateTime = Model.of();
    IModel<LocalTime> timeModel = Model.of();
    IModel<YearMonth> monthModel = Model.of();
    IModel<Year> yearModel = Model.of();
    IModel<LocalDate> dateIntervalle5jModel = Model.of();
    IModel<LocalDate> dateIntervalle15jModel = Model.of();
    IModel<LocalDate> dateIntervalle2mModel = Model.of();
    IModel<LocalDate> dateFormatRestreintModel = Model.of();
    IModel<LocalDate> datePlusieurMoisModel = Model.of();
    IModel<LocalDate> disableWeekend = Model.of();
    IModel<LocalDate> dateAjaxChange1Model = Model.of();
    IModel<LocalDate> dateAjaxChange2Model = Model.of();
    IModel<LocalDate> dateJsChange1Model = Model.of();
    IModel<LocalDate> dateJsChange2Model = Model.of();
    IModel<LocalDate> dateMarkers = Model.of();

    DatePickerVueField datepickerIntervalleFormulaireDebut =
        new DatePickerVueField(
            "datepickerIntervalleFormulaireDebut", datepickerIntervalleFormulaireDebutModel);
    DatePickerVueField datepickerIntervalleFormulaireFin =
        new DatePickerVueField(
            "datepickerIntervalleFormulaireFin", datepickerIntervalleFormulaireFinModel);

    DatePickerVueField datePickerAjaxChange1 =
        new DatePickerVueField("datePickerAjaxChange1", dateAjaxChange1Model);
    DatePickerVueField datePickerAjaxChange2 =
        new DatePickerVueField("datePickerAjaxChange2", dateAjaxChange2Model);
    DatePickerVueField datePickerJsChange1 =
        new DatePickerVueField("datePickerJsChange1", dateJsChange1Model);
    DatePickerVueField datePickerJsChange2 =
        new DatePickerVueField("datePickerJsChange2", dateJsChange2Model);
    datePickerJsChange1
        .getJsBuilder()
        .onUpdateModel(
            JsLiteral.of("value => %s.value = value".formatted(datePickerJsChange2.getVModel())));
    datePickerJsChange2
        .getJsBuilder()
        .onUpdateModel(
            JsLiteral.of("value => %s.value = value".formatted(datePickerJsChange1.getVModel())));
    add(
        form.add(
            new DatePickerVueField("datepickerClassic", dateClassiqueModel)
                .setRequired(true)
                .setLabel(new ResourceModel("tips.forms.datepickers.dateNaissance")),
            new DatePickerVueField(
                    "dateSansSaisieInput",
                    dateSansSaisieInput,
                    builder -> builder.placeholder(null).textInput(null))
                .setRequired(true)
                .setLabel(new ResourceModel("tips.forms.datepickers.dateNaissance")),
            new DatePickerVueField(
                    "datePickerWithValidation",
                    dateAutoApplyModel,
                    builder -> builder.autoApply(JsHelpers.of(false)))
                .setSelectText(new ResourceModel("tips.forms.datepickers.selectionner"))
                .setCancelText(new ResourceModel("tips.forms.datepickers.annuler"))
                .setLabel(new ResourceModel("tips.forms.datepickers.dateNaissance")),
            datepickerIntervalleFormulaireDebut
                .setDateMax(datepickerIntervalleFormulaireFin)
                .setLabel(new ResourceModel("tips.forms.datepickers.projet.debut")),
            datepickerIntervalleFormulaireFin
                .setDateMin(datepickerIntervalleFormulaireDebut)
                .setLabel(new ResourceModel("tips.forms.datepickers.projet.fin")),
            new DatePickerRangeVueField("datepickerRange", dateRange1Model, dateRange2Model)
                .setSelectText(new ResourceModel("tips.forms.datepickers.selectionner"))
                .setCancelText(new ResourceModel("tips.forms.datepickers.annuler"))
                .setLabel(new ResourceModel("tips.forms.datepickers.projet.dates")),
            new DatePickerRangeVueField(
                    "datepickerRangeSolo",
                    dateRange1Model,
                    dateRange2Model,
                    builder -> builder.multiCalendars(JsHelpers.of(false)))
                .setSelectText(new ResourceModel("tips.forms.datepickers.selectionner"))
                .setCancelText(new ResourceModel("tips.forms.datepickers.annuler"))
                .setLabel(new ResourceModel("tips.forms.datepickers.projet.dates")),
            new DatePickerRangeVueField(
                    "datepickerRangeThree",
                    dateRange1Model,
                    dateRange2Model,
                    builder -> builder.multiCalendars(JsHelpers.ofLiteral("{ count: 3 }")))
                .setSelectText(new ResourceModel("tips.forms.datepickers.selectionner"))
                .setCancelText(new ResourceModel("tips.forms.datepickers.annuler"))
                .setLabel(new ResourceModel("tips.forms.datepickers.projet.dates")),
            new DateTimePickerVueField("dateTimePicker", dateTime)
                .setLabel(new ResourceModel("tips.forms.datepickers.projet.dateTime")),
            new TimePickerVueField("timePicker", timeModel)
                .setLabel(new ResourceModel("tips.forms.datepickers.projet.time")),
            new MonthYearPickerVueField(
                    "monthPicker",
                    monthModel,
                    builder ->
                        builder.monthPicker(JsHelpers.of(true)).format(JsHelpers.of("MM/yyyy")))
                .setLabel(new ResourceModel("tips.forms.datepickers.projet.month")),
            new YearPickerVueField(
                    "yearPicker",
                    yearModel,
                    builder -> builder.yearPicker(JsHelpers.of(true)).format(JsHelpers.of("yyyy")))
                .setLabel(new ResourceModel("tips.forms.datepickers.projet.year")),
            new DatePickerVueField(
                    "datepickerIntervalle5j",
                    dateIntervalle5jModel,
                    builder ->
                        builder
                            .minDate(
                                JsHelpers.ofLiteral("new Date().setDate(new Date().getDate() - 5)"))
                            .maxDate(
                                JsHelpers.ofLiteral(
                                    "new Date().setDate(new Date().getDate() + 5)")))
                .setLabel(new ResourceModel("tips.forms.datepickers.intervalle.5j")),
            new DatePickerVueField(
                    "datepickerIntervalle15j",
                    dateIntervalle15jModel,
                    builder ->
                        builder
                            .minDate(
                                JsHelpers.ofLiteral(
                                    "new Date().setDate(new Date().getDate() - 15)"))
                            .maxDate(
                                JsHelpers.ofLiteral(
                                    "new Date().setDate(new Date().getDate() + 15)")))
                .setLabel(new ResourceModel("tips.forms.datepickers.intervalle.15j")),
            new DatePickerVueField(
                    "datepickerIntervalle2m",
                    dateIntervalle2mModel,
                    builder ->
                        builder
                            .minDate(
                                JsHelpers.ofLiteral(
                                    "new Date().setDate(new Date().getDate() - 60)"))
                            .maxDate(
                                JsHelpers.ofLiteral(
                                    "new Date().setDate(new Date().getDate() + 60)")))
                .setLabel(new ResourceModel("tips.forms.datepickers.intervalle.2m")),
            new DatePickerVueField(
                    "datepickerFormatRestreint",
                    dateFormatRestreintModel,
                    builder -> builder.format(JsHelpers.of("dd/MM/yy")))
                .setLabel(new ResourceModel("tips.forms.datepickers.format.restreint")),
            new DatePickerVueField(
                    "datepickerPlusieursMois",
                    datePlusieurMoisModel,
                    builder -> builder.multiCalendars(JsHelpers.of(true)))
                .setLabel(new ResourceModel("tips.forms.datepickers.dateRdv")),
            new DatePickerVueField(
                    "datepickerWeekendOff",
                    disableWeekend,
                    builder -> builder.disableWeekDays(JsHelpers.ofLiteral("[6,0]")))
                .setLabel(new ResourceModel("tips.forms.datepickers.dateRdv")),
            datePickerAjaxChange1
                .setLabel(new ResourceModel("tips.forms.datepickers.dateRdv"))
                .add(
                    new UpdateOnChangeAjaxEventBehavior()
                        .onChange(
                            new SerializableListener() {
                              private static final long serialVersionUID = 1L;

                              @Override
                              public void onBeforeRespond(
                                  Map<String, Component> map, AjaxRequestTarget target) {
                                dateAjaxChange2Model.setObject(dateAjaxChange1Model.getObject());
                                target.add(datePickerAjaxChange2);
                              }
                            })),
            datePickerAjaxChange2
                .setLabel(new ResourceModel("tips.forms.datepickers.dateRdv"))
                .add(
                    new UpdateOnChangeAjaxEventBehavior()
                        .onChange(
                            new SerializableListener() {
                              private static final long serialVersionUID = 1L;

                              @Override
                              public void onBeforeRespond(
                                  Map<String, Component> map, AjaxRequestTarget target) {
                                dateAjaxChange1Model.setObject(dateAjaxChange2Model.getObject());
                                target.add(datePickerAjaxChange1);
                              }
                            })),
            datePickerJsChange1.setLabel(new ResourceModel("tips.forms.datepickers.dateRdv")),
            datePickerJsChange2.setLabel(new ResourceModel("tips.forms.datepickers.dateRdv")),
            new DatePickerVueField(
                    "datePickerMarkers",
                    dateMarkers,
                    builder -> builder.markers(JsHelpers.ofLiteral(getMarkers())))
                .setLabel(new ResourceModel("tips.forms.datepickers.dateRdv")),
            new AjaxButton("save") {
              private static final long serialVersionUID = 1L;

              @Override
              protected void onSubmit(AjaxRequestTarget target) {
                try {
                  datePickerService.testNonNull(
                      List.of(
                          dateClassiqueModel.getObject(),
                          dateAutoApplyModel.getObject(),
                          datepickerIntervalleFormulaireDebutModel.getObject(),
                          datepickerIntervalleFormulaireFinModel.getObject(),
                          dateRange1Model.getObject(),
                          dateRange2Model.getObject(),
                          dateTime.getObject(),
                          timeModel.getObject(),
                          monthModel.getObject(),
                          dateIntervalle5jModel.getObject(),
                          dateIntervalle15jModel.getObject(),
                          dateIntervalle2mModel.getObject(),
                          dateFormatRestreintModel.getObject(),
                          datePlusieurMoisModel.getObject(),
                          disableWeekend.getObject(),
                          dateAjaxChange1Model.getObject(),
                          dateAjaxChange2Model.getObject(),
                          dateJsChange1Model.getObject(),
                          dateJsChange2Model.getObject(),
                          dateMarkers.getObject()));
                  Session.get().success(getString("common.success"));
                } catch (RestartResponseException e) { // NOSONAR
                  throw e;
                } catch (Exception e) {
                  Session.get().error(getString("common.error.unexpected"));
                }
                FeedbackUtils.refreshFeedback(target, getPage());
              }

              @Override
              protected void onError(AjaxRequestTarget target) {
                FeedbackUtils.refreshFeedback(target, getPage());
              }
            }));
  }

  public static IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(TestDatePickerPage.class);
  }

  @Override
  public Class<? extends WebPage> getFirstMenuPage() {
    return TestDatePickerPage.class;
  }

  public String getMarkers() {
    return """
[
  {
    date: new Date().setDate(new Date().getDate() + 1),
    type: 'dot',
    tooltip: [{ text: 'RDV 1', color: 'green' }],
  },
  {
    date: new Date().setDate(new Date().getDate() + 2),
    type: 'line',
    tooltip: [
      { text: 'RDV 1', color: 'blue' },
      { text: 'RDV 2', color: 'red' },
    ],
  },
  {
    date: new Date().setDate(new Date().getDate() + 3),
    type: 'dot',
    color: 'red',
  },
]
    """;
  }
}
