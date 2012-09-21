// Set the require.js configuration for your application.
requirejs.config({
  // Initialize the application with the main application file
  deps: ["main"],

  paths: {
    // JavaScript folders
    libs: "../lib",

    // Libraries
    jquery: "../lib/jquery-1.7.1.min",
    underscore: "../lib/underscore",
    backbone: "../lib/backbone",
    handlebars: "../lib/handlebars",

  },

  // non-amd compliant dependencies
  // https://github.com/jrburke/requirejs/wiki/Upgrading-to-RequireJS-2.0#wiki-shim
  shim: {
    'backbone': {
      deps: ["underscore", "jquery"],
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
