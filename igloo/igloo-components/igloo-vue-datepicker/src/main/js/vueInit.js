export const vModels = new Map();
export const functions = new Map();
export const options = new Map();
export const apps = new Map();

export function addVueModel(varName, model) {
  vModels.set(varName, Vue.ref(model));
}

export function addVueOptionModel(varName, model) {
  options.set(varName, Vue.ref(model));
}

export function addVueOnChangeMethode(varName, componentId, methodesImpl) {
  functions.set(varName, async value => {
    if (componentId) {
      // wait update of v-model before add onChange functions
      await new Promise((resolve, reject) => {
        if (vModels.get(componentId).value === value) {
          resolve();
        }
      });
      // wicket onChange is taken on div parent of input
      // manually call change event with propagation on input to trigger wicket onChange
      document.getElementById(componentId)?.
          getElementsByTagName('input')[0]?.dispatchEvent(
          new Event('change', { bubbles: true })); // set bubble true to allow event propagation

      // call optionals updates methodes
      if (typeof methodesImpl === 'function') {
        methodesImpl(value);
      }
    }
  });
}

// mount a new app to build vue datePicker.
// a new app is mount every times wicket reload the datepicker (load page or ajax)
// the old app is automatically drop when the component is remove from the document
export function mountVueAppWithId(id) {
  const datePicker = document.getElementById(id);
  const parent = document.getElementById(id).parentElement;
  const idParent = `${id}_vueAppEncloser`;
  // vue component have to be enclosed in a parent div to be build
  if (parent.id !== idParent) {
    const div = document.createElement('div');
    div.setAttribute('id', idParent);
    parent.insertBefore(div, datePicker);
    div.appendChild(datePicker);
  }
  // build app with all variables 
  const app = Vue.createApp({
    components: { VueDatePicker },
    setup() {
      return {
        ...Object.fromEntries(vModels),
        ...Object.fromEntries(functions),
        ...Object.fromEntries(options),
      };
    },
  });
  app.mount(`#${idParent}`);
  apps.set(id, app);
}

export function appendInputCssClass(id, AttrClass) {
  let uiRef = getUiRef(id);
  uiRef.value = {
    ...uiRef.value,
    input: uiRef.value.input
        ? uiRef.value.input.includes(AttrClass)
            ? uiRef.value.input
            : uiRef.value.input.concat(' ', AttrClass)
        : AttrClass,
  };
}

export function prependInputCssClass(id, AttrClass) {
  let uiRef = getUiRef(id);
  uiRef.value = {
    ...uiRef.value,
    input: uiRef.value.input
        ? uiRef.value.input.includes(AttrClass)
            ? uiRef.value.input
            : AttrClass.concat(' ', uiRef.value.input)
        : AttrClass,
  };
}

export function replaceInputCssClass(id, AttrClass) {
  let uiRef = getUiRef(id);
  uiRef.value = {
    ...uiRef.value,
    input: AttrClass,
  };
}

export function removeInputCssClass(id, AttrClass) {
  let uiRef = getUiRef(id);
  uiRef.value = {
    ...uiRef.value,
    input: uiRef.value?.input?.replace(AttrClass, ''),
  };
}

function getUiRef(id) {
  let uiRef = options.get(`${id}_ui`);
  if (!uiRef) {
    uiRef = Vue.ref({});
    options.set(`${id}_ui`, uiRef);
  }
  return uiRef;
}

// Replace tag "for" value in linked datePicker label if exist
// If not, the "for" of "wicket:for" is apply to the parent div and not to the generated input
export function replaceForToLabel(id) {
  document.evaluate(`//form//label[@for='${id}']`, document, null, XPathResult.ANY_TYPE, null)
    .iterateNext()
    ?.setAttribute("for", `dp-input-${id}`)
}

export function getAriaLabelType(type, hoursMessage, minutesMessage, secondsMessage) {
    switch (type) {
      case "hours":
        return hoursMessage;
      case "minutes":
        return minutesMessage;
      case "seconds":
        return secondsMessage;
      default:
        throw new Error(`Invalid type: ${type}`);
    }
}

export function getAriaLabelOverlay(overlay, overlayMessage, defaultMessage) {
  return overlay ? overlayMessage : defaultMessage;
}
