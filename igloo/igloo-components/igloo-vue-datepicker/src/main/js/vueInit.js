export const vModels = new Map();
export const functions = new Map();
export const apps = new Map();

/*export let app;*/

export function addVueModel(varName, model) {
    vModels.set(varName, Vue.ref(model));
}

export function addVueOnChangeMethode(varName, componentId, methodesImpl) {
    functions.set(varName, async value => {
        if (componentId) {
            // manualy call on change on input when vue data change
            // wait update of v-model
            await new Promise((resolve, reject) => {
                if (vModels.get(componentId).value === value) {
                    resolve();
                }
            })
            document.getElementById(componentId)
                ?.getElementsByTagName('input')[0]
                ?.dispatchEvent(new Event('change', { bubbles: true})); // set bubble true to allow event propagation
            
            // call optionals updates methodes
            if (typeof methodesImpl === 'function') {
                methodesImpl(value)
            }
        }
    });
}

export function addDivParent(varName, componentId, methodesImpl) {
    functions.set(varName, async value => {
        if (componentId) {
            // manualy call on change on input when vue data change
            // wait update of v-model
            await new Promise((resolve, reject) => {
                if (vModels.get(componentId).value === value) {
                    resolve();
                }
            })
            document.getElementById(componentId)
                ?.getElementsByTagName('input')[0]
                ?.dispatchEvent(new Event('change', { bubbles: true})); // set bubble true to allow event propagation

            // call optionals updates methodes
            if (typeof methodesImpl === 'function') {
                methodesImpl(value)
            }
        }
    });
}

export function mountVueAppWithId(id) {
    const datePicker = document.getElementById(id);
    const parent = document.getElementById(id).parentNode;
    const idParent = `${id}_vueAppEncloser`;
    if (parent.id !== idParent) {
        const div = document.createElement("div");
        div.setAttribute("id", idParent);
        parent.insertBefore(div, datePicker);
        div.appendChild(datePicker)
    }
    const app = Vue.createApp({
        components: {VueDatePicker},
        data() {
            return {
                ...Object.fromEntries(vModels),
                ...Object.fromEntries(functions)
            }
        }
    })
    app.mount(`#${idParent}`);
    apps.set(id, app);
}

