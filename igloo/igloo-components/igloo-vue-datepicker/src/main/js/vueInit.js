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
      // manualy call on change on input when vue data change
      // wait update of v-model
      await new Promise((resolve, reject) => {
        if (vModels.get(componentId).value === value) {
          resolve();
        }
      });
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

export function mountVueAppWithId(id) {
  const datePicker = document.getElementById(id);
  const parent = document.getElementById(id).parentNode;
  const idParent = `${id}_vueAppEncloser`;
  if (parent.id !== idParent) {
    const div = document.createElement('div');
    div.setAttribute('id', idParent);
    parent.insertBefore(div, datePicker);
    div.appendChild(datePicker);
  }
  const app = Vue.createApp({
    components: { VueDatePicker },
    data() {
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

