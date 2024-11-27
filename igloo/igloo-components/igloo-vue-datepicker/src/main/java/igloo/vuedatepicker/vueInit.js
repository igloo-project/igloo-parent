const vModels = new Map();
const functions = new Map();
let app;

function addVueModel(varName, model) {
    vModels.set(varName, Vue.ref(model));
}

function addVueMethode(varName, componentId, methodesImpl) {
    functions.set(varName, value => {
        document.getElementById(componentId)?.dispatchEvent(new Event('change'));
        methodesImpl(value)
    });
}

function mountVueApp() {
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
}

