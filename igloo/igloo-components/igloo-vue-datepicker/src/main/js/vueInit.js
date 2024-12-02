export const vModels = new Map();
export const functions = new Map();
export const apps = new Map();

/*export let app;*/

export function addVueModel(varName, model) {
    vModels.set(varName, Vue.ref(model));
}

export function addVueOnChangeMethode(varName, componentId, methodesImpl) {
    functions.set(varName, value => {
        if (componentId) {
            document.getElementById(componentId)?.dispatchEvent(new Event('change'));
            if (typeof methodesImpl === 'function') {
                methodesImpl(value)
            }
        }
    });
}

/*export function mountVueApp() {
    if (!app) {
        app = Vue.createApp({
            components: { VueDatePicker },
            data() {
                return {
                    ...Object.fromEntries(vModels),
                    ...Object.fromEntries(functions)
                }
            }
        });
        app.mount('body');
    }
}*/

export function mountVueAppWithId(id) {
    const app = Vue.createApp({
        components: {VueDatePicker},
        data() {
            return {
                ...Object.fromEntries(vModels),
                ...Object.fromEntries(functions)
            }
        }
    })
    app.mount(`#${id}`);
    apps.set(id, app);
}

