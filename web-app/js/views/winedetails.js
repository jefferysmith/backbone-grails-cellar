define([
        'underscore',
        'jquery',
        'models/winecollection',
        'text!templates/wine-details.html'
        ], function (_, $, wineList, wineDetailsTemplate) {

    var wineView = Backbone.View.extend({

        tagName : "div", // Not required since 'div' is the default if no el or tagName specified
        
        template: _.template(wineDetailsTemplate),

        initialize : function() {
            console.log('wineview init')
            this.model.bind("change", this.render, this);
        },

        render : function(eventName) {
            console.log('wineview render')
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
        },

        events : {
            "change input" : "change",
            "click .save" : "saveWine",
            "click .delete" : "deleteWine"
        },

        change : function(event) {
            var target = event.target;
            console.log('changing ' + target.id + ' from: ' + target.defaultValue + ' to: '
                    + target.value);
            // You could change your model on the spot, like this:
            // var change = {};
            // change[target.name] = target.value;
            // this.model.set(change);
        },

        saveWine : function() {
            this.model.set({
                name : $('#name').val(),
                grapes : $('#grapes').val(),
                country : $('#country').val(),
                region : $('#region').val(),
                year : $('#year').val(),
                description : $('#description').val()
            });
            if (this.model.isNew()) {
                var self = this;
                this.collection.create(this.model, {
                    success : function() {
                        self.goTo('wines/' + self.model.id, false);
                    }
                });
            } else {
                this.model.save();
            }

            return false;
        },

        deleteWine : function() {
            this.model.destroy({
                success : function() {
                    alert('Wine deleted successfully');
                    window.history.back();
                }
            });
            return false;
        }
    });
    return wineView;
});