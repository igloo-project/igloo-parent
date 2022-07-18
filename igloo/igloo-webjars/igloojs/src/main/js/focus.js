'use strict'

const PREFIX = "Focus: "

export function install() {
    [
        'DOMContentLoaded',
        'shown.bs.collapse',
        'shown.bs.dropdown',
        'shown.bs.modal',
        'shown.bs.offcanvas',
        'shown.bs.popover',
        'shown.bs.tab',
        'shown.bs.toast'
    ]
        .forEach(evt =>
            document.addEventListener(evt, event => {
                console.info(PREFIX + "install automatic focus on " + event.target)
                focus(event.target)
            })
        );
}

export function focus(parent) {
    console.info(PREFIX + "search focus from %o", parent)
    let priority = -1
    let elected = null
    parent.querySelectorAll("[data-focus]").forEach(el => {
        let elPriority = parseInt(el.dataset.focus)
        let skip = el.dataset.focusSkip
        if (isNaN(elPriority)) {
            elPriority = 0
        }
        if (skip === "ifnotempty" && el.value) {
            // field has to be skipped if not empty
            return
        }
        if (elPriority > priority) {
            console.debug(PREFIX + "find candidate %o with priority %d", el, elPriority)
            priority = elPriority
            elected = el
        }
    })
    if (elected) {
        focusElement(elected)
    } else {
        console.info(PREFIX + "no element found to focus in %o", parent)
    }
}

export function focusElement(el) {
    console.info(PREFIX + "focus triggered on %s", el)
    el.focus()
}
