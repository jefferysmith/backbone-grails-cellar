define([
  'backbone',
  'models/winemodel'
  ], function (Backbone, Wine) {
    "use strict";
    var wineCollection = Backbone.Collection.extend({
      model : Wine,
      url : "api/wines"
    });
    return wineCollection;
});
