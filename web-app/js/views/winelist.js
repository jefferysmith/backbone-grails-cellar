define([
        'jquery',
        'underscore',
        'backbone',
        'views/winelistitem'
        ], function ($, _, Backbone, WineListItemView) {
    var wineListView = Backbone.View.extend({

        tagName : 'ul',

        initialize : function() {
            console.log('winelistview init')
            this.model.bind("reset", this.render, this);
            var self = this;
            this.model.bind("add", function(wine) {
                $(self.el).append(new WineListItemView({
                    model : wine
                }).render().el);
            });
        },

        render : function(eventName) {
            console.log('winelistview render')
            _.each(this.model.models, function(wine) {
                $(this.el).append(new WineListItemView({
                    model : wine
                }).render().el);
            }, this);
            return this;
        }
    });
    return wineListView;
});
