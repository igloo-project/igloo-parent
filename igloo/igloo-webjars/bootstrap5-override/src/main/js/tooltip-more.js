import { default as BootstrapTooltip } from "bootstrap/js/src/tooltip"
import { isVisible } from "bootstrap/js/src/util"

class Tooltip extends BootstrapTooltip {
    constructor(element, options) {
        super(element, options)
        this._disposeTooltipIntervalId = null
    }

    show() {
        super.show()
        if (this.tip) {
            this._disposeTooltipIntervalId = setInterval(this._refreshTooltipVisibility.bind(this), 200)
        }
    }

    dispose() {
        super.dispose()
        if (this._disposeTooltipIntervalId) {
            clearInterval(this._disposeTooltipIntervalId)
        }
    }

    _refreshTooltipVisibility() {
        if (!this._element || !isVisible(this._element)) {
            // dispose triggers an error; using hide method
            this.hide()
        }
    }
}

export default Tooltip