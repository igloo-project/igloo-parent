/**
 * Minimal stand-in for the Wicket Ajax JavaScript (wicket-ajax-jquery.js, Wicket 10): the
 * Wicket.DOM.replace sequence and the Wicket.Event bus it publishes on. The datepicker does not
 * listen to these events, but replaying them keeps the simulated page close to a real one.
 */

type WicketSubscriber = (jqEvent: unknown, ...args: unknown[]) => void;

export interface WicketEventStub {
  Topic: { DOM_NODE_REMOVING: string; DOM_NODE_ADDED: string };
  subscribe(topic: string, subscriber: WicketSubscriber): void;
  publish(topic: string, ...args: unknown[]): void;
}

/**
 * Installs a fresh `window.Wicket.Event`. As in Wicket, subscribers receive the jQuery event first,
 * then the published arguments, and '*' subscribers receive every topic.
 */
export function installWicketStub(): WicketEventStub {
  const subscribers = new Map<string, WicketSubscriber[]>();
  const wicketEvent: WicketEventStub = {
    Topic: { DOM_NODE_REMOVING: '/dom/node/removing', DOM_NODE_ADDED: '/dom/node/added' },
    subscribe(topic, subscriber) {
      subscribers.set(topic, [...(subscribers.get(topic) ?? []), subscriber]);
    },
    publish(topic, ...args) {
      for (const subscribedTopic of [topic, '*']) {
        subscribers.get(subscribedTopic)?.forEach(subscriber => subscriber({ type: topic }, ...args));
      }
    },
  };
  Object.assign(window, { Wicket: { Event: wicketEvent } });
  return wicketEvent;
}

/**
 * Same sequence as Wicket.DOM.replace, applied to every component refreshed by an Ajax response:
 * publish '/dom/node/removing', replace the element markup, then publish '/dom/node/added'.
 */
export function wicketReplace(wicketEvent: WicketEventStub, element: Element, markup: string): void {
  wicketEvent.publish(wicketEvent.Topic.DOM_NODE_REMOVING, element);
  const template = document.createElement('template');
  template.innerHTML = markup.trim();
  element.replaceWith(template.content);
  const newElement = document.getElementById(element.id);
  if (newElement) {
    wicketEvent.publish(wicketEvent.Topic.DOM_NODE_ADDED, newElement);
  }
}
