'use strict'
export function init(date, url) {
    window.addEventListener('load', function() {
        const { createApp, ref } = Vue
        createApp({
            components: { VueDatePicker },
            data() {
                const dateModel = ref(date)
                const onchange = date => {
                    console.log(date);
                    Wicket.Ajax.ajax({
                        'u': url,
                        'm':'POST',
                        'ep': JSON.stringify({date: date.valueOf()})
                    })
                }

                return {
                    dateModel, onchange
                }
            }
        }).mount("body");
    });
}