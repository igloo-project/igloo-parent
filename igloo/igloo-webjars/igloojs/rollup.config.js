'use strict'

import resolve from '@rollup/plugin-node-resolve'
import babel from '@rollup/plugin-babel'

const path = require('path')

let file = process.env.FILE
let symbol = process.env.SYMBOL

const rollupConfig = {
  input: path.resolve(__dirname, `src/main/js/${file}.js`),
  output: {
    file: path.resolve(__dirname, `src/main/generated-js/dist/${file}.js`),
    format: 'umd',
    generatedCode: 'es2015',
    name: symbol,
    globals: {
      "bootstrap": "bootstrap",
      "lodash": "lodash"
    }
  },
  external: ['bootstrap', 'lodash'],
  plugins: [
    babel({
      exclude: 'node_modules/**',
      babelHelpers: 'bundled'
    }),
    resolve()
  ]
}

module.exports = rollupConfig
