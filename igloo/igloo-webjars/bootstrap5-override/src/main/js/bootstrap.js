// From bootstrap/js/index.umd.js with our replacement
import * as Popper from '@popperjs/core'

import Alert from 'bootstrap/js/src/alert'
import Button from 'bootstrap/js/src/button'
import Carousel from 'bootstrap/js/src/carousel'
import Collapse from 'bootstrap/js/src/collapse'
import Dropdown from 'bootstrap/js/src/dropdown'
import Offcanvas from 'bootstrap/js/src/offcanvas'
import Popover from 'bootstrap/js/src/popover'
import ScrollSpy from 'bootstrap/js/src/scrollspy'
import Tab from 'bootstrap/js/src/tab'
import Toast from 'bootstrap/js/src/toast'

import Modal from './modal-more'
import Tooltip from './tooltip-more'
import installTabListener from './tab-more'
import Confirm from './confirm'

installTabListener()

export default {
  Alert,
  Button,
  Carousel,
  Collapse,
  Dropdown,
  Modal,
  Offcanvas,
  Popover,
  ScrollSpy,
  Tab,
  Toast,
  Tooltip,
  Confirm,
  Popper
}
