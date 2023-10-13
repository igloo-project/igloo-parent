/**
 * --------------------------------------------------------------------------
 * Bootstrap (v5.2.3): util/focustrap.js
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
 * --------------------------------------------------------------------------
 */

import Config from "bootstrap/js/src/config"

/**
 * Constants
 */

const NAME = 'focustrap'

const Default = {
  autofocus: true,
  trapElement: null // The element to trap focus inside of
}

const DefaultType = {
  autofocus: 'boolean',
  trapElement: 'element'
}

/**
 * Class definition
 */

class FocusTrap extends Config {
  constructor(config) {
    super()
    this._config = this._getConfig(config)
    this._isActive = false
    this._lastTabNavDirection = null
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

  // Public
  activate() {
	  // overriden
  }

  deactivate() {
	  // overriden
  }
}

export default FocusTrap
