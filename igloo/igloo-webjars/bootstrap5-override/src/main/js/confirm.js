import { Modal } from "bootstrap"

import BaseComponent from "bootstrap/js/src/base-component"
import EventHandler from "bootstrap/js/src/dom/event-handler"
import SelectorEngine from "bootstrap/js/src/dom/selector-engine"
import Manipulator from "bootstrap/js/src/dom/manipulator"
import { typeCheckConfig } from "bootstrap/js/src/util/index"

const NAME = 'confirm'
const DATA_KEY = 'bs.confirm'
const EVENT_KEY = `.${DATA_KEY}`
const EVENT_CLICK = `click${EVENT_KEY}`
const EVENT_CONFIRM = `confirm${EVENT_KEY}`

const Default = {
    text: '',
    title: '',
    yesLabel: '',
    noLabel: '',
    yesIcon: '',
    noIcon: '',
    yesButton: '',
    noButton: '',
    noEscape: false,
    cssClassNames: '',
    template: '<div class="modal fade confirm" tabindex="-1">' +
        '<div class="modal-dialog">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
            '<div class="modal-title"></div>' +
            '<button class="btn-close" data-bs-dismiss="modal" type="button"></button>' +
        '</div>' +
        '<div class="modal-body"></div>' +
        '<div class="modal-footer">' +
            '<button class="btn btn-secondary" type="button" data-bs-dismiss="modal">' +
                '<span class="noIcon"></span>' +
            '</button>' +
            '<button class="btn btn-primary" type="button">' +
                '<span class="yesIcon"></span>' +
            '</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>'
}
const DefaultType = {
    text: 'string',
    title: 'string',
    yesLabel: 'string',
    noLabel: 'string',
    yesIcon: 'string',
    noIcon: 'string',
    yesButton: 'string',
    noButton: 'string',
    noEscape: 'boolean',
    cssClassNames: 'string',
    template: 'string'
}

class Confirm extends BaseComponent {
    constructor(element, config) {
        super(element, config)

        EventHandler.on(this._element, EVENT_CLICK, event => {
            this._onClick(event)
        })
        this._config = this._getConfig(config)
    }

    _onClick(event) {
        this._initializeModalElement()
        this.modal.show()
    }

    _initializeModalElement() {
        if (!this.modal) {
            const modalElement = this._createModalElement()
            document.body.append(modalElement)
            this.modal = new bootstrap.Modal(modalElement)
        }
    }

    _createModalElement(content) {
        const wrapper = document.createElement('div')
        wrapper.innerHTML = this._config.template
        const modal = wrapper.children[0]

        // set before replacing class
        SelectorEngine.findOne('.btn-secondary', modal).append(this._config.noLabel)
        SelectorEngine.findOne('.btn-primary', modal).append(this._config.yesLabel)

        // TODO - set class for buttons
        if (this._config.noButton) {
            SelectorEngine.findOne('.btn-secondary', modal).classList = [this._config.noButton]
        }
        if (this._config.yesButton) {
            SelectorEngine.findOne('.btn-primary', modal).classList = [this._config.yesButton]
        }
        if (this._config.yesIcon) {
            SelectorEngine.findOne('.yesIcon', modal).classList = [this._config.yesIcon]
        } else {
            SelectorEngine.findOne('.yesIcon', modal).remove()
        }
        if (this._config.noIcon) {
            SelectorEngine.findOne('.noIcon', modal).classList = [this._config.noIcon]
        } else {
            SelectorEngine.findOne('.noIcon', modal).remove()
        }
        SelectorEngine.findOne('.modal-title', modal).textContent = this._config.title
        if (this._config.noEscape) {
            SelectorEngine.findOne('.modal-body', modal).textContent = this._config.text
        } else {
            SelectorEngine.findOne('.modal-body', modal).innerHTML = this._config.text
        }

        EventHandler.on(SelectorEngine.findOne('.btn-primary', modal), EVENT_CLICK, event => this._triggerConfirm(event))

        return modal
    }

    _triggerConfirm(event) {
        EventHandler.trigger(this._element, EVENT_CONFIRM)
        this.modal.hide()
    }

    _getConfig(config) {
        config = {
          ...Default,
          ...Manipulator.getDataAttributes(this._element),
          ...(typeof config === 'object' ? config : {})
        }
        typeCheckConfig(NAME, config, DefaultType)
        return config
    }
    

    // Getters
    static get Default() {
      return Default
    }
  
    static get DefaultType() {
      return DefaultType
    }

    static get NAME() {
      return NAME
    }
}

export default Confirm