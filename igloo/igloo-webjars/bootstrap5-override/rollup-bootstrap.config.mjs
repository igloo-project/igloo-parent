'use strict';

import babel from '@rollup/plugin-babel';
import resolve from '@rollup/plugin-node-resolve';
import commonjs from '@rollup/plugin-commonjs';
import path from 'path';
import { fileURLToPath } from 'url';
import { importManager } from "rollup-plugin-import-manager";

const file = process.env.FILE;
const symbol = process.env.SYMBOL;
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

/* 
 * From bootstrap configuration, with:
 * - override for focustrap to allow focus on external widget (like select2) (importManager)
 * - commonjs plugin for commonjs to es6 rewrites
 * - node resolver to find bootstrap sources
 */
export default {
  input: path.resolve(__dirname, `src/main/js/${file}.js`),
  output: {
    file: path.resolve(__dirname, `src/main/generated-js/js/dist/${file}.js`),
    format: 'umd',
    generatedCode: 'es2015',
    // symbol name extracted from command line (like 'bootstrap')
    name: symbol,
    globals: {},
  },
  external: [],
  plugins: [
    // resolve from node_modules
    resolve(),
    // rewrite focustrap import when needed
    //
    // src/main/js/dom/data.js file must be kept sync with bootstrap and allowed multiple js components on one element
    // https://github.com/twbs/bootstrap/blob/main/js/src/dom/data.js (USE THE APPROPRIATE VERSION)
    //
    // src/main/js/util/focustrap.js file must be kept sync with bootstrap and customized to allow third-party focus
    // https://github.com/twbs/bootstrap/blob/main/js/src/util/focustrap.js (USE THE APPROPRIATE VERSION)
    // imports must be rewritten (../ -> bootstrap/js/src)
    // _handleFocusin customization must be added
    //
    // modal, tooltip replacement
    // -> enableDismissTrigger and defineJQueryPlugin are faked with noop functions
    // -> ModalMore performs EventHandler.off to remove Modal installation
    importManager({
      // display modified imports for checks
      showDiff: true,
      units: [{
          file: ["**/base-component.js"],
          createModule: "../../../../src/main/js/dom/data.js",
          actions: {
            "select": "defaultMembers",
            "add": "Data",
          },
          replace: {
            rawModule: "./dom/data.js"
          }
        },
        // replace ./util/focustrap.js imports when needed
        {
          file: ["**/modal.js", "**/offcanvas.js"],
          createModule: "../../../../src/main/js/util/focustrap.js",
          actions: {
            "select": "defaultMembers",
            "add": "FocusTrap",
          },
          replace: {
            rawModule: "./util/focustrap.js"
          }
        },
        {
          file: ["**/modal.js"],
          createModule: "../../../../src/main/js/util/bypass-utils.js",
          actions: {
            "select": "members",
            "add": "enableDismissTrigger",
          },
          replace: {
            rawModule: "./util/component-functions.js"
          }
        },
        {
          file: ["**/modal.js", "**/tooltip.js"],
          rawModule: "./util/index.js",
          actions: {
            "select": "member",
            "name": "defineJQueryPlugin",
            "remove": null,
          }
        },
        {
          file: ["**/modal.js", "**/tooltip.js"],
          createModule: "../../../../src/main/js/util/bypass-utils.js",
          actions: {
            "select": "members",
            "add": "defineJQueryPlugin",
          }
        }
      ]
    }),
    commonjs(),
    babel({
      exclude: 'node_modules/**',
      babelHelpers: 'bundled',
    }),
  ]
};
