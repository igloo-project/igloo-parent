const vModels = new Map();

function addVueModel(componentId, model) {
    vModels.set(componentId, Vue.ref(model));
}

function mountVueApp() {
    const app = Vue.createApp({
        components: { VueDatePicker },
        data() {
            return Object.fromEntries(vModels)
        }
    });
    app.mount('body');
}

