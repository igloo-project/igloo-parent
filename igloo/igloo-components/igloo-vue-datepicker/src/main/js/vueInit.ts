/**
 * Renders VueDatePicker components in Wicket pages. Bundled by rollup as the global `vueInit`.
 *
 * For each datepicker field, Wicket renders an empty `<vue-date-picker id="...">` tag, the host, and
 * a script calling `vueInit.mount(id, config)`. The component is rendered inside the host, which
 * stays owned by Wicket. When the host leaves the page, replaced by a Wicket Ajax response or
 * removed by another script, the component is unmounted.
 */
import {VueDatePicker} from '@vuepic/vue-datepicker';
// @ts-ignore: no typings for CSS, extracted into vueInit.css by rollup
import '@vuepic/vue-datepicker/dist/main.css';
import type {Locale} from 'date-fns/locale';
import {enUS} from 'date-fns/locale/en-US';
import {fr} from 'date-fns/locale/fr';
import {
  type Component,
  computed,
  defineComponent,
  h,
  nextTick,
  type PropType,
  render,
  shallowReactive,
  type VNodeChild
} from 'vue';

/** What Wicket renders for a datepicker, see VueDatePickerBehavior#renderHead. */
export interface DatePickerConfig {
  inputName: string;
  language: string;
  initialValue: unknown;
  props: () => Record<string, unknown>;
  onUpdateModel?: (value: unknown) => void;
  disabled?: boolean;
  slots?: DatePickerSlots;
}

/** VueDatePicker slots, by name: functions rendering them with `Vue.h`, given the slot props. */
export type DatePickerSlots = Record<string, (slotProps: any) => VNodeChild>;

interface InputClassChanges {
  added: string[];
  removed: string[];
}

//
// State
//

const hosts = new Map<string, HTMLElement>();
const values = shallowReactive(new Map<string, unknown>());
const inputClassChanges = shallowReactive(new Map<string, InputClassChanges>());
const locales = shallowReactive(
  new Map<string, Locale>([
    ['fr', fr],
    ['en', enUS],
  ]),
);
let pageObserver: MutationObserver | undefined;

//
// Public API: called by the scripts Wicket renders, and by project scripts
//

/** Renders the datepicker inside its host, the Wicket component tag with markup id `id`. */
export function mount(id: string, config: DatePickerConfig): void {
  const host = document.getElementById(id);
  if (!host) {
    return;
  }
  observePage();
  // previous rendering of this component: Wicket replaced its host and runs this script for the new
  // one, possibly before the page observer noticed the old host left the page
  unmount(id);
  host.replaceChildren();
  values.set(id, config.initialValue);
  render(h(WicketDatePicker, { id, config }), host);
  hosts.set(id, host);
  linkLabels(id);
}

/** Unmounts the datepicker `id`, if mounted. The host is left empty in the page. */
export function unmount(id: string): void {
  const host = hosts.get(id);
  if (!host) {
    return;
  }
  render(null, host);
  hosts.delete(id);
  values.delete(id);
  inputClassChanges.delete(id);
}

/**
 * Value of the datepicker `id`. Read inside a props function, it makes the props follow the value,
 * e.g. `minDate: vueInit.getModelValue('startDate')`.
 */
export function getModelValue(id: string): unknown {
  return values.get(id);
}

/**
 * Sets the value of the datepicker `id` from client-side code. Wicket is not notified: the value is
 * submitted with the form.
 */
export function setModelValue(id: string, value: unknown): void {
  if (hosts.has(id)) {
    values.set(id, value);
  }
}

/**
 * Registers a date-fns locale for a session language, e.g. `registerLocale(de)`, or
 * `registerLocale(enGB, 'en')` to replace the bundled English (US) locale.
 */
export function registerLocale(locale: Locale, language: string = locale.code): void {
  locales.set(language, locale);
}

/** Adds CSS classes to the input of the datepicker `id` (see IVueComponent). */
export function appendInputCssClass(id: string, cssClass: string): void {
  changeInputClasses(id, cssClass, true);
}

/** Removes CSS classes from the input of the datepicker `id`, including configured ones. */
export function removeInputCssClass(id: string, cssClass: string): void {
  changeInputClasses(id, cssClass, false);
}

//
// Internals
//

/**
 * Wraps VueDatePicker with what Wicket needs: input name and id, value shared with the other
 * datepickers, change notification, CSS classes and locale.
 */
const WicketDatePicker = defineComponent({
  name: 'WicketDatePicker',
  props: {
    id: { type: String, required: true },
    config: { type: Object as PropType<DatePickerConfig>, required: true },
  },
  setup(props) {
    const pickerProps = computed(() => props.config.props());

    /** Called when the user picks a value. */
    async function onUpdateModelValue(value: unknown): Promise<void> {
      values.set(props.id, value);
      // Wicket reads the value of the input: wait until the new value is rendered into it
      await nextTick();
      // Wicket listens to `change` on the host, filtered on the input name (FormComponentChangeAjaxEventBehavior)
      document.getElementById(`dp-input-${props.id}`)?.dispatchEvent(new Event('change', { bubbles: true }));
      props.config.onUpdateModel?.(value);
    }

    return () => {
      const { ui, inputAttrs, ...otherProps } = pickerProps.value as {
        ui?: Record<string, unknown>;
        inputAttrs?: Record<string, unknown>;
      };
      return h(VueDatePicker as Component, {
        locale: locales.get(props.config.language),
        ...otherProps,
        ...(props.config.disabled ? { disabled: true } : {}),
        ui: { ...ui, input: inputClasses(props.id, ui?.['input']) },
        inputAttrs: { ...inputAttrs, name: props.config.inputName, id: `dp-input-${props.id}` },
        modelValue: values.get(props.id),
        'onUpdate:modelValue': onUpdateModelValue,
      }, props.config.slots);
    };
  },
});

/**
 * Watches the page, started on first mount, whoever changes it: Wicket Ajax responses, Bootstrap
 * removing a modal, project scripts.
 *
 * - Unmounts the datepickers whose host left the page. Without it, such a datepicker would stay
 *   alive: its reactive effects, its calendar teleported into the body, and its entries in the maps
 *   above. The observer runs once the changes are done: a host moved elsewhere in the page is still
 *   connected to the document then, and stays mounted.
 * - Points again to the inputs the labels added to the page, e.g. a label refreshed alone by Wicket,
 *   rendered pointing to the host.
 */
function observePage(): void {
  if (pageObserver) {
    return;
  }
  pageObserver = new MutationObserver(records => {
    if (records.some(record => record.removedNodes.length > 0)) {
      [...hosts].filter(([, host]) => !host.isConnected).forEach(([id]) => unmount(id));
    }
    // the datepickers re-render often: only look for labels when some were added
    if (
      records.some(record =>
        [...record.addedNodes].some(
          node => node instanceof Element && (node.matches('label') || node.querySelector('label') !== null),
        ),
      )
    ) {
      hosts.forEach((_host, id) => linkLabels(id));
    }
  });
  pageObserver.observe(document.body, { childList: true, subtree: true });
}

/** wicket:for points the labels to the host: point them to the input rendered inside. */
function linkLabels(id: string): void {
  document.querySelectorAll<HTMLLabelElement>(`label[for="${id}"]`).forEach(label => {
    label.htmlFor = `dp-input-${id}`;
  });
}

function changeInputClasses(id: string, cssClass: string, add: boolean): void {
  // Wicket sends these calls for every Vue component of the page, rendered or not
  if (!hosts.has(id)) {
    return;
  }
  const classes = toClassList(cssClass);
  const without = (list: string[]): string[] => list.filter(item => !classes.includes(item));
  const { added, removed } = inputClassChanges.get(id) ?? { added: [], removed: [] };
  inputClassChanges.set(
    id,
    add
      ? { added: [...without(added), ...classes], removed: without(removed) }
      : { added: without(added), removed: [...without(removed), ...classes] },
  );
}

/** Configured classes of the input, with the changes made by Wicket. */
function inputClasses(id: string, configured: unknown): string[] {
  const classes = new Set(toClassList(configured));
  const changes = inputClassChanges.get(id);
  changes?.added.forEach(item => classes.add(item));
  changes?.removed.forEach(item => classes.delete(item));
  return [...classes];
}

/**
 * Classes as a space separated string, an array, or an object enabling the classes of its keys,
 * like the Vue class binding. Normalized to an array, the only form VueDatePicker accepts with strings.
 */
function toClassList(classes: unknown): string[] {
  if (typeof classes === 'string') {
    return classes.split(/\s+/).filter(Boolean);
  }
  if (Array.isArray(classes)) {
    return classes.flatMap(toClassList);
  }
  if (classes && typeof classes === 'object') {
    return Object.entries(classes).flatMap(([name, enabled]) => (enabled ? toClassList(name) : []));
  }
  return [];
}
