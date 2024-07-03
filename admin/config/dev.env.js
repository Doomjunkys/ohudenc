'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  VUE_APP_TITLE: '"ITKK控制台"',
  VUE_APP_BASE_URL:'"/"',
  VUE_APP_ADMIN_BASE_URL:'"/admin/"'
})
