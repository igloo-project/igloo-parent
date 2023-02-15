'use strict'

import * as bootstrap from "bootstrap"

class ModalMore extends bootstrap.Modal {
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
        this._isTransitioning = false;
    }
}

export default ModalMore