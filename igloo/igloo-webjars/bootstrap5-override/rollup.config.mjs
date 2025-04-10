'use strict';

import babel from '@rollup/plugin-babel';
import resolve from '@rollup/plugin-node-resolve';
import commonjs from '@rollup/plugin-commonjs';
import path from 'path';
import { fileURLToPath } from 'url';

const file = process.env.FILE;
const symbol = process.env.SYMBOL;
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export default {
  input: path.resolve(__dirname, `src/main/js/${file}.js`),
  output: {
    file: path.resolve(__dirname, `src/main/generated-js/js/dist/${file}.js`),
    format: 'umd',
    generatedCode: 'es2015',
    name: symbol,
    globals: {
      'bootstrap': 'bootstrap',
    },
  },
  external: ['bootstrap'],
  plugins: [
    commonjs(),
    babel({
      exclude: 'node_modules/**',
      babelHelpers: 'bundled',
    }),
    resolve(),
  ],
};
