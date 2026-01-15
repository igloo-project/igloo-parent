import {type InputAttributesConfig, VueDatePicker} from '@vuepic/vue-datepicker';

// @ts-ignore
import '@vuepic/vue-datepicker/dist/main.css';

import {type App, createApp, type Ref, ref} from 'vue';

import type {Locale} from "date-fns/locale";
import * as dateFnsLocales from 'date-fns/locale';

// Export all variables used by the app view to be exposed and modified by wicket
export const vModels = new Map<string, Ref<Date>>();
export const functions = new Map<string, Function>();
export const options = new Map<string, Ref>();
export const apps = new Map<string, App>();
export const localeRef: Ref<Locale | undefined> = ref<Locale>();

export function addVueModel(varName: string, model: Date): void {
  vModels.set(varName, ref(model));
}

export function addVueOptionModel(varName: string, model: any): void {
  options.set(varName, ref(model));
}

export function setLocale(code: string): void {
  localeRef.value = Object.values<Locale>(dateFnsLocales).find(l => l.code === code);
}

export function addVueOnChangeMethode(varName: string, componentId: string, methodesImpl: string | Function): void {
  functions.set(varName, async (value: any) => {
    if (componentId) {
      // wait update of v-model before add onChange functions
      await new Promise((resolve, reject) => {
        if (vModels.get(componentId)?.value === value) {
          resolve("Async model is ready");
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
export function mountVueAppWithId(id: string): void {
  const datePicker = document.getElementById(id) as Node;
  const parent: HTMLElement | null | undefined = document.getElementById(id)?.parentElement;
  const idParent = `${id}_vueAppEncloser`;
  // vue component have to be enclosed in a parent div to be build
  if (parent?.id !== idParent) {
    const div: HTMLDivElement = document.createElement('div');
    div.setAttribute('id', idParent);
    parent?.insertBefore(div, datePicker);
    div.appendChild(datePicker);
  }
  // build app with all variables 
  const app: App = createApp({
    components: { VueDatePicker },
    setup() {
      return {
        ...Object.fromEntries(vModels),
        ...Object.fromEntries(functions),
        ...Object.fromEntries(options),
        localeRef
      };
    },
  });
  app.mount(`#${idParent}`);
  apps.set(id, app);
}

export function appendInputCssClass(id: string, attrClass: string): void {
  let uiRef = getUiRef(id);
  uiRef.value = {
    ...uiRef.value,
    input: uiRef.value.input
        ? uiRef.value.input.includes(attrClass)
            ? uiRef.value.input
            : uiRef.value.input.concat(' ', attrClass)
        : attrClass,
  };
}

export function prependInputCssClass(id: string, AttrClass: string): void {
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

export function replaceInputCssClass(id: string, AttrClass: string): void {
  let uiRef = getUiRef(id);
  uiRef.value = {
    ...uiRef.value,
    input: AttrClass,
  };
}

export function removeInputCssClass(id: string, AttrClass: string): void {
  let uiRef = getUiRef(id);
  uiRef.value = {
    ...uiRef.value,
    input: uiRef.value?.input?.replace(AttrClass, ''),
  };
}

function getUiRef(id: string): any {
  let uiRef: any = options.get(`${id}_ui`);
  if (!uiRef) {
    uiRef = ref({});
    options.set(`${id}_ui`, uiRef);
  }
  return uiRef;
}

// Replace tag "for" value in linked datePicker label if exist
// If not, the "for" of "wicket:for" is apply to the parent div and not to the generated input
export function replaceForOnLabel(id: string): void {
  (document.evaluate(`//form//label[@for='${id}']`, document, null, XPathResult.ANY_TYPE, null)
  .iterateNext() as Element)
?.setAttribute("for", `dp-input-${id}`)
}

export function getAriaLabelType(type: 'hours' | 'minutes' | 'seconds', hoursMessage: string, minutesMessage: string, secondsMessage: string): string {
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

export function getAriaLabelOverlay(overlay: string, overlayMessage: string, defaultMessage: string): string {
  return overlay ? overlayMessage : defaultMessage;
}

/**
 * Add id and name to the input.
 * <li> Name is mandatory by Wicket to get input
 * <li> Id is override and use to link the label from
 */
export function setupInput(id: string, path: string): void {
  const inputAttrsRef: Ref<InputAttributesConfig> = options.get(`${id}_input_attrs`) as Ref<InputAttributesConfig>
  inputAttrsRef.value = {
    ...inputAttrsRef.value,
    name: path,
    id: `dp-input-${id}`
  };
}
