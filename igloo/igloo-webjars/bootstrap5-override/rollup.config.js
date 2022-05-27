'use strict'

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
      "@bootstrap": "bootstrap"
    }
  },
  external: ['@bootstrap']
}

module.exports = rollupConfig
