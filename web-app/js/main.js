require([
  'jquery',
  'underscore',
  'backbone',
  'routers/router',
  'views/header',
  ], function ($, _, Backbone, AppRouter, HeaderView ) {
    console.log('starting main');

    var appRouter = new AppRouter();

    // extend backbone classes for additional functionality
    // Maybe should be done somewhere else?
    Backbone.View.prototype.close = function() {
        console.log('Closing view ' + this);
        if (this.beforeClose) {
            this.beforeClose();
        }
        this.remove();
        this.unbind();
    };
    Backbone.View.prototype.goTo = function (loc) {
        appRouter.navigate(loc, true);
    };

    // start rendering the content
    $('#header').html(new HeaderView().render().el);

});
