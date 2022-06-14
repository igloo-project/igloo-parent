'use strict'

window.addEventListener("DOMContentLoaded", () => {
    document.addEventListener("shown.bs.tab", event => {
        if (event.target && event.target.matches('button[role="tab"]')) {
            history.pushState({}, '', event.target.dataset["bsTarget"])
        }
    })
    const hash = document.location.hash
    if (hash) {
        const targetTab = document.querySelector('button[data-bs-target="' + hash + '"]');
        if (targetTab) {
            const tab = new bootstrap.Tab(targetTab)
            tab.show()
        }
    }
})