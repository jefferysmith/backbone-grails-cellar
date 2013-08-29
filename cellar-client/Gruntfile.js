// Wrapper function with one parameter
module.exports = function(grunt) {

    var serverWebapp = '../cellar-service/web-app'

    grunt.initConfig({
        // make it easy to refer to properties defined inside the package.json file
        pkg: grunt.file.readJSON('package.json'),

        clean: {
            // remove copied client app files from grails 'web-app' directory
            dev: {
                options: {force: true},
                src: [
                    serverWebapp + '/images',
                    serverWebapp + '/lib',
                    serverWebapp + '/scripts',
                    serverWebapp + '/styles',
                    serverWebapp + '/templates',
                    serverWebapp + '/index.html'
                ]
            }
        },

        copy: {
            // copy client application into grails 'web-app' directory
            dev: {
                files: [
                    {expand: true, cwd: 'app/',src: ['**'], dest: serverWebapp }
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
    grunt.loadNpmTasks('grunt-contrib-clean');

    grunt.registerTask('dev', ['clean:dev', 'copy:dev']);
    grunt.registerTask('default', ['jshint']);
};
