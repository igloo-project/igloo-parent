'use strict'

import * as bootstrap from "bootstrap"

class Offcanvas extends bootstrap.Offcanvas {
    _initializeFocusTrap() {
        return { activate: function() { }, deactivate: function() { } }
    }
}

export default Offcanvas