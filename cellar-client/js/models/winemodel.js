define([
  'backbone'
], function (Backbone) {
    "use strict";
    var wineModel = Backbone.Model.extend({
        urlRoot : "api/wines",
        defaults : {
            "id" : null,
            "name" : "",
            "grapes" : "",
            "country" : "USA",
            "region" : "California",
            "year" : "",
            "description" : "",
            "picture" : ""
        }
    });
    return wineModel;
});
