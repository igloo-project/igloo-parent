'use strict'

import babel from '@rollup/plugin-babel';
import resolve from '@rollup/plugin-node-resolve';
import commonjs from '@rollup/plugin-commonjs';

const file = process.env.FILE
const symbol = process.env.SYMBOL

export default {
  input: `src/main/js/${file}.js`,
  output: {
    file: `src/main/generated-js/dist/${file}.js`,
    format: 'umd',
    generatedCode: 'es2015',
    name: symbol,
    globals: {}
  },
  external: [],
  plugins: [
    commonjs(),
    resolve(),
    babel({
      exclude: 'node_modules/**',
      babelHelpers: 'bundled'
    })
  ]
}
