// Wrapper function with one parameter
module.exports = function(grunt) {

    grunt.initConfig({
        // make it easy to refer to properties within the package.json file
        pkg: grunt.file.readJSON('package.json'),

        jshint: {
            options: {
                trailing: true
            },
            target: {
                src : ['app/scripts/**/*.js']
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-jshint');

    grunt.registerTask('default', ['jshint']);
};
