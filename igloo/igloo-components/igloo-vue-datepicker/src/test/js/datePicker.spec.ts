import {de} from 'date-fns/locale/de';
import {enGB} from 'date-fns/locale/en-GB';
import {afterEach, beforeEach, describe, expect, it, vi} from 'vitest';
import {
  datePickerInput,
  datePickerInstance,
  flush,
  listenWicketChange,
  openMenu,
  pickValue
} from './support/dom';
import {
  createDatePickerTester,
  type DatePickerTester,
  inputName,
  type PickerOptions
} from './support/datePickerTester';
import {installWicketStub, type WicketEventStub, wicketReplace} from './support/wicket';

/**
 * Behaviour of the datepicker in a Wicket page, observed through the DOM only. The vueInit API is
 * reached through the DatePickerTester, so these scenarios guard against regressions across
 * refactorings.
 */
describe('vue datepicker', () => {
  const JAN_15 = new Date(2026, 0, 15);
  const JAN_20 = new Date(2026, 0, 20);

  let wicketEvent: WicketEventStub;
  let tester: DatePickerTester;
  const cleanups: Array<() => void> = [];

  beforeEach(async () => {
    wicketEvent = installWicketStub();
    // vueInit keeps its state at module level: start each test from a fresh module
    vi.resetModules();
    tester = createDatePickerTester(await import('../../main/js/vueInit'));
  });

  afterEach(() => {
    cleanups.splice(0).forEach(cleanup => cleanup());
    document.body.innerHTML = '';
  });

  /** Renders a form with the given datepickers and their labels, then runs their header scripts. */
  function renderPage(pickers: Record<string, PickerOptions>): void {
    const fields = Object.entries(pickers)
      // label id as rendered by Wicket for wicket:for
      .map(([id, options]) => `<label id="${id}-w-lbl" for="${id}">${id}</label>${tester.markup(id, options)}`)
      .join('');
    document.body.innerHTML = `
      <form id="form">
        <div id="container">${fields}</div>
        <div id="other"></div>
      </form>`;
    Object.entries(pickers).forEach(([id, options]) => tester.renderHead(id, options));
  }

  /** Ajax refresh of a single datepicker: Wicket replaces its markup, then replays its header scripts. */
  function refresh(id: string, options: PickerOptions = {}): void {
    wicketReplace(wicketEvent, document.getElementById(id)!, tester.markup(id, options));
    tester.renderHead(id, options);
  }

  function listenWicket(id: string): string[] {
    const listener = listenWicketChange(id, inputName(id));
    cleanups.push(listener.stop);
    return listener.values;
  }

  function localeCode(id: string): string | undefined {
    return (datePickerInstance(id).props.locale as { code?: string } | undefined)?.code;
  }

  describe('rendering', () => {
    it('renders inside the Wicket component tag, which stays in the page', () => {
      renderPage({ start: {} });

      // Wicket binds its Ajax listeners to this element: it must not be replaced
      const host = document.getElementById('start');
      expect(host?.tagName).toBe('VUE-DATE-PICKER');
      expect(host?.contains(datePickerInput('start'))).toBe(true);
    });

    it('displays the initial value with the configured format', async () => {
      renderPage({ start: { value: JAN_15.getTime() } });
      // VueDatePicker formats its initial value after mounting
      await flush();

      expect(datePickerInput('start')?.value).toBe('15/01/2026');
    });

    it('submits the input under the Wicket input name', async () => {
      renderPage({ start: {} });
      await flush();

      expect(datePickerInput('start')?.name).toBe(inputName('start'));
    });

    it('disables the datepicker of a field disabled in Wicket', async () => {
      renderPage({ start: { disabled: true }, end: {} });
      await flush();

      expect(datePickerInstance('start').props.disabled).toBe(true);
      expect(datePickerInput('start')?.disabled).toBe(true);
      expect(datePickerInput('end')?.disabled).toBe(false);
    });

    it('points the field label to the rendered input', async () => {
      renderPage({ start: {} });
      await flush();

      const input = datePickerInput('start')!;
      expect(input.id).not.toBe('');
      expect(document.querySelector('label')?.htmlFor).toBe(input.id);
    });

    it('points a label refreshed alone by Wicket to the rendered input again', async () => {
      renderPage({ start: {} });
      await flush();

      wicketReplace(wicketEvent, document.getElementById('start-w-lbl')!, '<label id="start-w-lbl" for="start">start</label>');
      await flush();

      expect(document.querySelector<HTMLLabelElement>('#start-w-lbl')?.htmlFor).toBe(datePickerInput('start')!.id);
    });

  });

  describe('locales', () => {
    it('applies the French locale to a French session', () => {
      renderPage({ start: { language: 'fr' } });

      expect(localeCode('start')).toBe('fr');
    });

    it('applies the US English locale to an English session', () => {
      renderPage({ start: { language: 'en' } });

      expect(localeCode('start')).toBe('en-US');
    });

    it('falls back to the VueDatePicker default, US English, for other languages', () => {
      renderPage({ start: { language: 'de' } });

      expect(localeCode('start')).toBe('en-US');
    });

    it('applies a locale registered by the project', () => {
      tester.registerLocale(de);
      renderPage({ start: { language: 'de' } });

      expect(localeCode('start')).toBe('de');
    });

    it('lets the project replace a bundled locale', () => {
      tester.registerLocale(enGB, 'en');
      renderPage({ start: { language: 'en' } });

      expect(localeCode('start')).toBe('en-GB');
    });
  });

  describe('user change', () => {
    it('notifies Wicket once the input shows the picked value', async () => {
      renderPage({ start: {} });
      const sentToWicket = listenWicket('start');

      pickValue('start', JAN_20);
      await flush();

      expect(sentToWicket).toEqual(['20/01/2026']);
    });

    it('calls the onUpdateModel callback with the picked value', async () => {
      const onUpdateModel = vi.fn();
      renderPage({ start: { onUpdateModel } });

      pickValue('start', JAN_20);
      await flush();

      expect(onUpdateModel).toHaveBeenCalledOnce();
      expect(onUpdateModel).toHaveBeenCalledWith(JAN_20);
    });
  });

  describe('linked datepickers', () => {
    it('updates the min date when the linked datepicker changes', async () => {
      renderPage({ start: {}, end: { minDateOf: 'start' } });

      pickValue('start', JAN_15);
      await flush();

      expect(datePickerInstance('end').props.minDate).toEqual(JAN_15);
    });

    it('keeps following the linked datepicker after it is refreshed alone', async () => {
      renderPage({ start: {}, end: { minDateOf: 'start' } });
      refresh('start');

      pickValue('start', JAN_20);
      await flush();

      expect(datePickerInstance('end').props.minDate).toEqual(JAN_20);
    });

    it('lets client-side code set a datepicker value without notifying Wicket', async () => {
      renderPage({ start: {}, end: {} });
      const sentToWicket = listenWicket('end');

      tester.setValueFromJs('end', JAN_20);
      await flush();

      expect(datePickerInput('end')?.value).toBe('20/01/2026');
      expect(sentToWicket).toEqual([]);
    });
  });

  describe('input CSS classes', () => {
    it('adds and removes a class while keeping the configured ones', async () => {
      renderPage({ start: {} });

      tester.appendInputCssClass('start', 'is-invalid');
      await flush();
      expect([...datePickerInput('start')!.classList]).toEqual(expect.arrayContaining(['form-control', 'is-invalid']));

      tester.removeInputCssClass('start', 'is-invalid');
      await flush();
      expect(datePickerInput('start')!.classList).toContain('form-control');
      expect(datePickerInput('start')!.classList).not.toContain('is-invalid');
    });

    it('ignores a datepicker absent from the page', () => {
      // Wicket sends these calls for every Vue component of the page, rendered or not
      expect(() => tester.appendInputCssClass('missing', 'is-invalid')).not.toThrow();
      expect(() => tester.removeInputCssClass('missing', 'is-invalid')).not.toThrow();
    });

    it('accepts configured classes written as an object', async () => {
      renderPage({ start: { inputClass: { 'form-control': true, 'form-control-sm': false } } });

      tester.appendInputCssClass('start', 'is-invalid');
      await flush();

      const classes = [...datePickerInput('start')!.classList];
      expect(classes).toEqual(expect.arrayContaining(['form-control', 'is-invalid']));
      expect(classes).not.toContain('form-control-sm');
    });
  });

  describe('Wicket Ajax lifecycle', () => {
    it('unmounts the datepicker when Wicket removes its container', async () => {
      renderPage({ start: {} });
      const instance = datePickerInstance('start');

      wicketReplace(wicketEvent, document.getElementById('container')!, '<div id="container"></div>');
      await flush();

      expect(document.getElementById('start')).toBeNull();
      expect(instance.isUnmounted).toBe(true);
    });

    it('replaces the datepicker when Wicket refreshes it alone', async () => {
      renderPage({ start: {} });
      const previous = datePickerInstance('start');

      refresh('start', { value: JAN_15.getTime() });
      await flush();

      expect(previous.isUnmounted).toBe(true);
      expect(datePickerInput('start')?.value).toBe('15/01/2026');

      const sentToWicket = listenWicket('start');
      pickValue('start', JAN_20);
      await flush();
      expect(sentToWicket).toEqual(['20/01/2026']);
    });

    it('keeps the datepicker mounted when Wicket replaces an unrelated element', () => {
      renderPage({ start: {} });
      const instance = datePickerInstance('start');

      wicketReplace(wicketEvent, document.getElementById('other')!, '<div id="other"></div>');

      expect(instance.isUnmounted).toBe(false);
      expect(datePickerInput('start')).not.toBeNull();
    });

    it('unmounts the datepicker when another script removes it from the page', async () => {
      renderPage({ start: {} });
      const instance = datePickerInstance('start');
      openMenu('start');
      await flush();

      // no Wicket event: e.g. a modal removed by Bootstrap
      document.getElementById('container')!.remove();

      await vi.waitFor(() => expect(instance.isUnmounted).toBe(true));
      await vi.waitFor(() => expect(document.querySelector('.dp--menu')).toBeNull());
    });

    it('keeps the datepicker mounted when another script moves it in the page', async () => {
      renderPage({ start: {} });
      const instance = datePickerInstance('start');

      document.getElementById('other')!.append(document.getElementById('start')!);
      await flush();

      expect(instance.isUnmounted).toBe(false);
      expect(datePickerInput('start')).not.toBeNull();
    });

    it('removes the open menu, teleported to the body, when Wicket removes the datepicker', async () => {
      renderPage({ start: {} });
      openMenu('start');
      await flush();
      expect(document.querySelector('.dp--menu')).not.toBeNull();

      wicketReplace(wicketEvent, document.getElementById('container')!, '<div id="container"></div>');

      // the menu leaves through a Vue transition, completed on the next animation frame
      await vi.waitFor(() => expect(document.querySelector('.dp--menu')).toBeNull());
    });
  });
});
