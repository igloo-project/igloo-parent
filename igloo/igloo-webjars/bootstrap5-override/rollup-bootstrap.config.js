'use strict'

import resolve from '@rollup/plugin-node-resolve'
import babel from '@rollup/plugin-babel'
import { importManager } from "rollup-plugin-import-manager";


const path = require('path')

let file = process.env.FILE
let symbol = process.env.SYMBOL

const rollupConfig = {
  input: path.resolve(__dirname, `src/main/js/${file}.js`),
  output: {
    file: path.resolve(__dirname, `src/main/generated-js/js/dist/${file}.js`),
    format: 'umd',
    generatedCode: 'es2015',
    name: symbol,
    globals: {
      "bootstrap": "bootstrap"
    }
  },
  external: [], // bootstrap must be included
  plugins: [
    importManager({
      units: [{
        file: "**/modal.js",
        createModule: "../../../../src/main/js/focustrap.js",
        actions: {
          "select": "defaultMembers",
          "add": "FocusTrap",
        },
        replace: {
          rawModule: "./util/focustrap"
        }
      }]
    }),
    babel({
      exclude: 'node_modules/**',
      babelHelpers: 'bundled'
    }),
    resolve()
  ]
}

module.exports = rollupConfig
