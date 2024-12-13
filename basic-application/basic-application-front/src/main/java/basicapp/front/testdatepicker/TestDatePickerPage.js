function init() {
  const app = Vue.createApp({
    components: { VueDatePicker },
    setup() {
      const dateClassiqueRef = vueInit.vModels.get('datepickerClassic');
      const dateRangeRef = vueInit.vModels.get('datepickerRange');
      const dateRangeRef2 = vueInit.vModels.get('datepickerRange2');
      // Attention nom de la variable définie par java avec le tag à cause de @update:model-value parsé par wicket (comme wicket:id)
      const datepickerRange_onChange = value => {
        dateClassiqueRef.value = value[0];
      };

      return {
        dateClassiqueRef,
        dateRangeRef,
        dateRangeRef2,
        datepickerRange_onChange,
      };
    },
  });
  app.mount('body');
}
