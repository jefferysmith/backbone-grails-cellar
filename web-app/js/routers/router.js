define([
    'jquery',
    'backbone',
    'underscore',
    'views/header',
    'views/winelist',
    'views/winedetails',
    'models/winecollection'
    ], function ($, Backbone, _, HeaderView, WineListView, WineDetailsView, WineCollection ) {
    var appRouter = Backbone.Router.extend({

        initialize : function() {
            console.log('initializing router');
            Backbone.View.prototype.close = function() {
                console.log('Closing view ' + this);
                if (this.beforeClose) {
                    this.beforeClose();
                }
                this.remove();
                this.unbind();
            };
            Backbone.history.start();
            $('#header').html(new HeaderView().render().el);
        },

        routes : {
            "" : "list",
            "wines/new" : "newWine",
            "wines/:id" : "wineDetails"
        },

        list : function() {
            this.before();
        },

        wineDetails : function(id) {
            this.before(function() {
                var wine = WineCollection.get(id);
                showView('#content', new WineDetailsView({
                    model : wine
                }));
            });
        },

        newWine : function() {
            this.before(function() {
                showView('#content', new WineDetailsView({
                    model : new Wine()
                }));
            });
        },

        showView : function(selector, view) {
            if (this.currentView)
                this.currentView.close();
            $(selector).html(view.render().el);
            this.currentView = view;
            return view;
        },

        before : function(callback) {
            if (this.wineList) {
                if (callback)
                    callback();
            } else {
                this.wineList = new WineCollection();
                this.wineList.fetch({
                    success : function() {
                        $('#sidebar').html(new WineListView({
                            model : WineCollection
                        }).render().el);
                        if (callback)
                            callback();
                    }
                });
            }
        }

    });
    return appRouter;
});
