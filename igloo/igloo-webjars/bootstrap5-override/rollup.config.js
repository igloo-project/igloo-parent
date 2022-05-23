'use strict'

const path = require('path')

let file = process.env.FILE

const rollupConfig = {
  input: path.resolve(__dirname, `src/main/js/${file}.js`),
  output: {
    file: path.resolve(__dirname, `target/js/js/dist/${file}.js`),
    format: 'umd',
    generatedCode: 'es2015',
    name: 'bootstrapMore',
    globals: {
      "@bootstrap": "bootstrap"
    }
  },
  external: ['@bootstrap']
}

module.exports = rollupConfig
