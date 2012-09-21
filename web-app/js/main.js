require([
         'routers/router',
         'utils'
         ], function (AppRouter, Utils) {
    console.log('starting main');
    Utils.loadTemplates([ 'header', 'wine-details', 'wine-list-item' ])
    new AppRouter();
});
