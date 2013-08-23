// Wrapper function with one parameter
module.exports = function(grunt) {

    var serverWebapp = '../cellar-service/web-app'

    grunt.initConfig({
        // make it easy to refer to properties within the package.json file
        pkg: grunt.file.readJSON('package.json'),

        copy: {
            dev: {
                files: [
                    {src: 'app/images/**', dest: serverWebapp + '/images/'}
                ]
            }
        },

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
    grunt.loadNpmTasks('grunt-contrib-copy');

    grunt.registerTask('dev', ['copy']);
    grunt.registerTask('default', ['jshint']);
};
