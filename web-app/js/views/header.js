define([
  'jquery',
  'underscore',
  'utils',
  'backbone'
  ], function ($, _, tpl, Backbone) {

    var headerView = Backbone.View.extend({

        initialize : function() {
            console.log('headerview init')
            var hdr = tpl.get('header');
            this.template = _.template(hdr);
//            this.template = _.template(tpl.get('header'));
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
            app.navigate("wines/new", true);
            return false;
        }
    });
    return headerView;
});