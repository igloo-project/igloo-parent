'use strict';

// No Babel, unlike igloojs and bootstrap5-override: the datepicker needs Vue 3, which needs a modern
// browser (no Internet Explorer, Proxy required), so transpiling the bundle for older browsers
// would not make it run on them. The bundle keeps the ESNext syntax of tsconfig.json.

import resolve from '@rollup/plugin-node-resolve';
import commonjs from '@rollup/plugin-commonjs';
import replace from '@rollup/plugin-replace';
import css from 'rollup-plugin-css-only';
import terser from '@rollup/plugin-terser';
import typescript from '@rollup/plugin-typescript';

const file = process.env.FILE;
const symbol = process.env.SYMBOL;

export default {
  input: `src/main/js/${file}.ts`,
  output: {
    file: `src/main/generated-js/dist/${file}.js`,
    format: 'iife',
    name: symbol,
    globals: { vue: 'Vue' },
  },
  external: ['vue'], // CRUCIAL -> import from cdn
  plugins: [
    replace({
      'process.env.NODE_ENV': JSON.stringify(
          process.env.NODE_ENV || 'development'),
      preventAssignment: true,
    }),
    commonjs(),
    resolve(),
    typescript({
      tsconfig: './tsconfig.json',      // Use this tsconfig file to configure TypeScript compilation
    }),
    css({ output: `${file}.css` }), // Extract CSS from datepicker
    terser(),
  ],
};
