/* eslint-env node */

'use strict'

import babel from '@rollup/plugin-babel';
import istanbul from 'rollup-plugin-istanbul';
import nodeResolve from '@rollup/plugin-node-resolve';
import path from 'path';
import ip from 'ip';
import {browsers, browsersKeys} from './browsers.mjs'

const ENV = process.env
const BROWSERSTACK = Boolean(ENV.BROWSERSTACK)
const DEBUG = Boolean(ENV.DEBUG)
const JQUERY_TEST = Boolean(ENV.JQUERY)

const frameworks = [
  'jasmine'
]

const plugins = [
  'karma-jasmine',
  'karma-rollup-preprocessor'
]

const reporters = ['dots']

const detectBrowsers = {
  usePhantomJS: false,
  postDetection(availableBrowser) {
    // On CI just use Chrome
    if (ENV.CI === true) {
      return ['ChromeHeadless']
    }

    if (availableBrowser.includes('Chrome')) {
      return DEBUG ? ['Chrome'] : ['ChromeHeadless']
    }

    if (availableBrowser.includes('Chromium')) {
      return DEBUG ? ['Chromium'] : ['ChromiumHeadless']
    }

    if (availableBrowser.includes('Firefox')) {
      return DEBUG ? ['Firefox'] : ['FirefoxHeadless']
    }

    throw new Error('Please install Chrome, Chromium or Firefox')
  }
}

const conf = {
  basePath: '../../..',
  port: 9876,
  colors: true,
  autoWatch: false,
  singleRun: true,
  concurrency: Number.POSITIVE_INFINITY,
  client: {
    clearContext: false
  },
  files: [
    'src/test/js/unit/**/*.js'
  ],
  preprocessors: {
    'src/test/js/unit/**/*.spec.js': ['rollup']
  },
  rollupPreprocessor: {
    plugins: [
      istanbul({
        exclude: [
          'node_modules/**',
          'src/test/js/unit/**/*.spec.js',
          'src/test/js/helpers/**/*.js',
        ]
      }),
      babel({
        // Only transpile our source code
        exclude: 'node_modules/**',
        // Inline the required helpers in each file
        babelHelpers: 'inline'
      }),
      nodeResolve()
    ],
    output: {
      format: 'iife',
      name: 'bootstrapTest',
      sourcemap: 'inline',
      generatedCode: 'es2015'
    }
  }
}

if (BROWSERSTACK) {
  conf.hostname = ip.address()
  conf.browserStack = {
    username: ENV.BROWSER_STACK_USERNAME,
    accessKey: ENV.BROWSER_STACK_ACCESS_KEY,
    build: `bootstrap-${ENV.GITHUB_SHA ? ENV.GITHUB_SHA.slice(0, 7) + '-' : ''}${new Date().toISOString()}`,
    project: 'Bootstrap',
    retryLimit: 2
  }
  plugins.push('karma-browserstack-launcher', 'karma-jasmine-html-reporter')
  conf.customLaunchers = browsers
  conf.browsers = browsersKeys
  reporters.push('BrowserStack', 'kjhtml')
} else if (JQUERY_TEST) {
  frameworks.push('detectBrowsers')
  plugins.push(
    'karma-chrome-launcher',
    'karma-firefox-launcher',
    'karma-detect-browsers'
  )
  conf.detectBrowsers = detectBrowsers
  conf.files = []
} else {
  frameworks.push('detectBrowsers')
  plugins.push(
    'karma-chrome-launcher',
    'karma-firefox-launcher',
    'karma-detect-browsers',
    'karma-coverage-istanbul-reporter'
  )
  reporters.push('coverage-istanbul')
  conf.detectBrowsers = detectBrowsers
  conf.coverageIstanbulReporter = {
    dir: path.resolve(__dirname, '../coverage/'),
    reports: ['lcov', 'text-summary'],
    thresholds: {
      emitWarning: false,
      global: {
        statements: 90,
        branches: 89,
        functions: 90,
        lines: 90
      }
    }
  }

  if (DEBUG) {
    conf.hostname = ip.address()
    plugins.push('karma-jasmine-html-reporter')
    reporters.push('kjhtml')
    conf.singleRun = false
    conf.autoWatch = true
  }
}

conf.frameworks = frameworks
conf.plugins = plugins
conf.reporters = reporters

export default karmaConfig => {
  conf.logLevel = karmaConfig.LOG_ERROR
  karmaConfig.set(conf)
}
