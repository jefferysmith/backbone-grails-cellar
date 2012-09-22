define([
        'jquery',
        'underscore',
        'backbone',
        'text!templates/wine-list-item.html'
        ], function ($, _, Backbone, WineListItemTemplate) {
    var wineListItemView = Backbone.View.extend({

        tagName : "li",
        
        template: _.template(WineListItemTemplate),

        initialize : function() {
            console.log('winelistitemview init')
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