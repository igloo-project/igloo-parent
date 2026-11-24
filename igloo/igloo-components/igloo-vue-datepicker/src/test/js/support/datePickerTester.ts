import type {Locale} from 'date-fns/locale';

/**
 * Reproduces what VueDatePickerBehavior sends to the browser for a datepicker: the markup of the
 * Wicket component, and the header script replayed on page load and on each Ajax refresh.
 *
 * This is the only test file aware of the vueInit API: the scenarios go through it.
 */

type VueInit = typeof import('../../../main/js/vueInit');

export interface PickerOptions {
  value?: number | null;
  language?: string;
  minDateOf?: string;
  onUpdateModel?: (value: unknown) => void;
  disabled?: boolean;
  inputClass?: unknown;
}

export const DATE_FORMAT = 'dd/MM/yyyy';

/** Wicket input name of the datepicker, deliberately different from its markup id. */
export function inputName(id: string): string {
  return `form:${id}`;
}

export interface DatePickerTester {
  /** HTML of the Wicket component tag. */
  markup(id: string, options?: PickerOptions): string;
  /** Header contributions of the component, in rendering order. */
  renderHead(id: string, options?: PickerOptions): void;
  /** Sets a datepicker value from client-side code, as an onUpdateModel callback may do. */
  setValueFromJs(id: string, value: unknown): void;
  appendInputCssClass(id: string, cssClass: string): void;
  removeInputCssClass(id: string, cssClass: string): void;
  registerLocale(locale: Locale, language?: string): void;
}

export function createDatePickerTester(vueInit: VueInit): DatePickerTester {
  return {
    markup(id) {
      return `<vue-date-picker id="${id}"></vue-date-picker>`;
    },

    renderHead(id, options = {}) {
      vueInit.mount(id, {
        inputName: inputName(id),
        language: options.language ?? 'fr',
        initialValue: options.value ?? null,
        props: () => ({
          textInput: true,
          teleport: true,
          formats: { input: DATE_FORMAT },
          ui: { input: options.inputClass ?? 'form-control', menu: 'dp--menu-bs' },
          ...(options.minDateOf ? { minDate: vueInit.getModelValue(options.minDateOf) } : {}),
        }),
        ...(options.disabled ? { disabled: true } : {}),
        ...(options.onUpdateModel ? { onUpdateModel: options.onUpdateModel } : {}),
      });
    },

    setValueFromJs(id, value) {
      vueInit.setModelValue(id, value);
    },

    appendInputCssClass(id, cssClass) {
      vueInit.appendInputCssClass(id, cssClass);
    },

    removeInputCssClass(id, cssClass) {
      vueInit.removeInputCssClass(id, cssClass);
    },

    registerLocale(locale, language) {
      vueInit.registerLocale(locale, language);
    },
  };
}
