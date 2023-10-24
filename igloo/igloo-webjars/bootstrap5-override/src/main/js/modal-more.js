import { default as BootstrapModal } from "bootstrap/js/src/modal"
import EventHandler from 'bootstrap/js/src/dom/event-handler.js'
import SelectorEngine from 'bootstrap/js/src/dom/selector-engine.js'
import {
  defineJQueryPlugin, isVisible
} from 'bootstrap/js/src/util/index.js'
import { enableDismissTrigger } from 'bootstrap/js/src/util/component-functions.js'

class Modal extends BootstrapModal {
    show(relatedTarget) {
        this._appendToBody()
        // override current transitioning state
        this._ignoreTransitioning()
        super.show(relatedTarget)
    }

    hide() {
        // override current transitioning state
        this._ignoreTransitioning()
        super.hide()
    }

    _hideModal() {
        super._hideModal()
        if (this._parent) {
            this._parent.appendChild(this._element)
        }
    }

    _appendToBody() {
        this._parent = this._element.parentNode
        document.body.appendChild(this._element)
    }

    _ignoreTransitioning() {
        this._isTransitioning = false
    }
}

const DATA_KEY = 'bs.modal'
const EVENT_KEY = `.${DATA_KEY}`
const DATA_API_KEY = '.data-api'

const EVENT_HIDDEN = `hidden${EVENT_KEY}`
const EVENT_SHOW = `show${EVENT_KEY}`
const EVENT_CLICK_DATA_API = `click${EVENT_KEY}${DATA_API_KEY}`

const OPEN_SELECTOR = '.modal.show'
const SELECTOR_DATA_TOGGLE = '[data-bs-toggle="modal"]'

EventHandler.off(document, EVENT_CLICK_DATA_API, SELECTOR_DATA_TOGGLE);
EventHandler.on(document, EVENT_CLICK_DATA_API, SELECTOR_DATA_TOGGLE, function (event) {
  const target = SelectorEngine.getElementFromSelector(this)

  if (['A', 'AREA'].includes(this.tagName)) {
    event.preventDefault()
  }

  EventHandler.one(target, EVENT_SHOW, showEvent => {
    if (showEvent.defaultPrevented) {
      // only register focus restorer if modal will actually get shown
      return
    }

    EventHandler.one(target, EVENT_HIDDEN, () => {
      if (isVisible(this)) {
        this.focus()
      }
    })
  })

  // avoid conflict when clicking modal toggler while another one is open
  const alreadyOpen = SelectorEngine.findOne(OPEN_SELECTOR)
  if (alreadyOpen) {
    Modal.getInstance(alreadyOpen).hide()
  }

  const data = Modal.getOrCreateInstance(target)

  data.toggle(this)
})

enableDismissTrigger(Modal)

/**
 * jQuery
 */

defineJQueryPlugin(Modal)

export default Modal