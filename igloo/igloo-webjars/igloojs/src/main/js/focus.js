'use strict'

const PREFIX = "Focus: "

export function install() {
    document.addEventListener("DOMContentLoaded", event => {
        console.info(PREFIX + "install automatic focus")
        focus(document)
    })
}

export function focus(parent) {
    console.info(PREFIX + "search focus from %o", parent)
    let priority = -1
    let elected = null
    let empty = false
    parent.querySelectorAll("[data-focus]").forEach(el => {
        let elPriority = parseInt(el.dataset.focus)
        let skip = el.dataset.focusSkip
        if (isNaN(elPriority)) {
            elPriority = 0
        }
        if (skip === "ifnotempty" && el.value) {
            // field has to be skipped if empty
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