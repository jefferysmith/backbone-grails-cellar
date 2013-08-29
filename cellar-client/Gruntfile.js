// Wrapper function with one parameter
module.exports = function(grunt) {

    var serverWebapp = '../cellar-service/web-app'

    grunt.initConfig({
        // make it easy to refer to properties defined inside the package.json file
        pkg: grunt.file.readJSON('package.json'),

        clean: {
            dev: {
                options: {force: true},
                src: [
                    serverWebapp + '/images',
                    serverWebapp + '/lib',
                    serverWebapp + '/scripts',
                    serverWebapp + '/styles',
                    serverWebapp + '/templates',
                    serverWebapp + 'index.html'
                ]
            }
        },

        copy: {
            dev: {
                files: [
                    {expand: true, cwd: 'app/images/',src: ['**'], dest: serverWebapp + '/images/'},
                    {expand: true, cwd: 'app/lib/',src: ['**'], dest: serverWebapp + '/lib/'},
                    {expand: true, cwd: 'app/scripts/',src: ['**'], dest: serverWebapp + '/scripts/'},
                    {expand: true, cwd: 'app/styles/',src: ['**'], dest: serverWebapp + '/styles/'},
                    {expand: true, cwd: 'app/templates/',src: ['**'], dest: serverWebapp + '/templates/'},
                    {expand: true, cwd: 'app/',src: ['index.html'], dest: serverWebapp }
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
