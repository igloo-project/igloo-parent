import { defineConfig } from 'vitest/config';

// Vue-dependent packages are inlined so that every `import 'vue'` goes through the alias below:
// a single Vue instance, as in production where all of them share the global `Vue`.
const VUE_DEPENDENT_PACKAGES = [
  'vue',
  'vue-demi',
  '@vuepic/vue-datepicker',
  '@vueuse/core',
  '@vueuse/shared',
  '@floating-ui/vue',
];

export default defineConfig({
  resolve: {
    alias: [
      // one Vue file for every import, whatever the conditions each package resolves 'vue' with.
      // This build includes the template compiler, unused: vueInit renders with render functions.
      { find: /^vue$/, replacement: 'vue/dist/vue.esm-bundler.js' },
    ],
    dedupe: ['vue'],
  },
  test: {
    environment: 'jsdom',
    include: ['src/test/js/**/*.spec.ts'],
    server: {
      deps: {
        inline: VUE_DEPENDENT_PACKAGES,
      },
    },
  },
});
