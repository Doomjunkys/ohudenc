'use strict'
const merge = require('webpack-merge')
const devEnv = require('./dev.env')

module.exports = merge(devEnv, {
  NODE_ENV: '"testing"',
  VUE_APP_TITLE: '"ITKK控制台"',
  VUE_APP_BASE_URL:'"/"'
})
