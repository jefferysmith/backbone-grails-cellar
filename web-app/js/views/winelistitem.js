define([
        'jquery',
        'underscore',
        'backbone'
        ], function ($, _, Backbone) {
    var wineListItemView = Backbone.View.extend({

        tagName : "li",

        initialize : function() {
            console.log('winelistitemview init')
            this.template = _.template(tpl.get('wine-list-item'));
            this.model.bind("change", this.render, this);
            this.model.bind("destroy", this.close, this);
        },

        render : function(eventName) {
            console.log('winelistitemview render')
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
        }
    });
    return wineListItemView;
});