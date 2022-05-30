import * as bootstrap from "bootstrap"

class ModalMore extends bootstrap.Modal {
    show(relatedTarget) {
        this._appendToBody()
        super.show(relatedTarget)
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
}

export default ModalMore