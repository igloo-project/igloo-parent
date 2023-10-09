'use strict'

import * as bootstrap from "bootstrap"

class OffcanvasMore extends bootstrap.Offcanvas {
    _initializeFocusTrap() {
        return { activate: function() { }, deactivate: function() { } }
    }
}

export default OffcanvasMore