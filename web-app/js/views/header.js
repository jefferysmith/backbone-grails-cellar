define([
  'jquery',
  'underscore',
  'backbone',
  'text!templates/header.html'
  ], function ($, _, Backbone, headerTemplate) {

    var headerView = Backbone.View.extend({
        
        template: _.template(headerTemplate),

        initialize : function() {
            console.log('headerview init');
        },

        render : function(eventName) {
            console.log('headerview render')
            $(this.el).html(this.template());
            return this;
        },

        events : {
            "click .new" : "newWine"
        },

        newWine : function(event) {
            this.goTo("wines/new", true);
            return false;
        }
    });
    return headerView;
});