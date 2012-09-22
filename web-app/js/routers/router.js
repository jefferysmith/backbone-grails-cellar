define([
    'jquery',
    'backbone',
    'underscore',
    'models/winemodel',
    'views/winelist',
    'views/winedetails',
    'models/winecollection'
    ], function ($, Backbone, _, Wine, WineListView, WineDetailsView, WineCollection ) {
    var appRouter = Backbone.Router.extend({

        initialize : function() {
            console.log('initializing router');
            // router initialize
            Backbone.history.start();
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
            var that = this;
            this.before(function() {
                var wine = that.wineList.get(id);
                that.showView('#content', new WineDetailsView({
                    model : wine
                }));
            });
        },

        newWine : function() {
            var that = this
            this.before(function() {
                // uses special options 'model' and 'collection' to attach to the view
                // http://backbonejs.org/#View-constructor
                that.showView('#content', new WineDetailsView({
                    model : new Wine(),
                    collection: that.wineList
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
                    success : function(collection, response) {
                        $('#sidebar').html(new WineListView({
                            model : collection
                        }).render().el);
                        if (callback)
                            callback();
                    },
                    error: function(collection, response) {
                        console.log('wineCollection not fetched...');
                    }
                });
            }
        }

    });
    return appRouter;
});
