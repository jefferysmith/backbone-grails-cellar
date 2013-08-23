// Set the require.js configuration for your application.
requirejs.config({
  // Initialize the application with the main application file
  deps: ["main"],

  paths: {
    // JavaScript folders
    libs: "../lib",

    // Libraries
    jquery: "../lib/jquery-1.9.1",
    underscore: "../lib/underscore-1.4.4",
    backbone: "../lib/backbone-0.9.10",
    handlebars: "../lib/handlebars",
    text: "../lib/text-2.0.5",
    json2: "../lib/json2",

    // Templates
    templates: "../templates"
  },

  // non-amd compliant dependencies
  // https://github.com/jrburke/requirejs/wiki/Upgrading-to-RequireJS-2.0#wiki-shim
  shim: {
    'backbone': {
      deps: ["underscore", "jquery", "json2"],
      exports: "Backbone"
    },

    'underscore': {
      exports: "_"
    },

    'handlebars': {
      exports: "Handlebars"
    }
  }
});
