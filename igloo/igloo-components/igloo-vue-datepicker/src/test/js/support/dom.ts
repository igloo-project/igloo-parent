import type {ComponentInternalInstance} from 'vue';

/**
 * Helpers reaching the rendered VueDatePicker through the DOM only, so that the scenarios do not
 * depend on how vueInit mounts it.
 */

/** Waits until pending promises and Vue DOM updates are flushed. */
export function flush(): Promise<void> {
  return new Promise(resolve => setTimeout(resolve, 0));
}

export function datePickerInput(id: string): HTMLInputElement | null {
  return document.getElementById(id)?.querySelector('input') ?? null;
}

/**
 * The VueDatePicker component instance rendered for `id`: the closest component above the input that
 * receives the `update:model-value` listener. Relies on `__vueParentComponent`, set by Vue in
 * development builds, which is what Vitest runs.
 */
export function datePickerInstance(id: string): ComponentInternalInstance {
  const input = datePickerInput(id) as (HTMLInputElement & { __vueParentComponent?: ComponentInternalInstance }) | null;
  let instance = input?.__vueParentComponent ?? null;
  while (instance && !instance.vnode.props?.['onUpdate:modelValue']) {
    instance = instance.parent;
  }
  if (!instance) {
    throw new Error(`No VueDatePicker rendered for '${id}'`);
  }
  return instance;
}

/** Simulates the user picking a value: the datepicker emits it, exactly as after a click or a typed date. */
export function pickValue(id: string, value: Date | Date[] | null): void {
  datePickerInstance(id).emit('update:model-value', value);
}

/** Opens the datepicker menu through the method VueDatePicker exposes. */
export function openMenu(id: string): void {
  (datePickerInstance(id).exposed as { openMenu: () => void }).openMenu();
}

/**
 * Records the input values sent to Wicket: Wicket listens to `change` on the component element, with a
 * child selector on the input name (see FormComponentChangeAjaxEventBehavior). Returns the recorded values
 * and a function removing the listener.
 */
export function listenWicketChange(id: string, inputName: string): { values: string[]; stop: () => void } {
  const values: string[] = [];
  const listener = (event: Event): void => {
    const target = event.target as HTMLInputElement;
    if (document.getElementById(id)?.contains(target) && target.name === inputName) {
      values.push(target.value);
    }
  };
  document.addEventListener('change', listener);
  return { values, stop: () => document.removeEventListener('change', listener) };
}
