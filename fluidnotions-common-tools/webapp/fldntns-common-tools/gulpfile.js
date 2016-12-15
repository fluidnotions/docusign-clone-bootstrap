var gulp = require('gulp');
var browserify = require('browserify');
var source = require('vinyl-source-stream');
var buffer = require('gulp-buffer');
var watch = require('gulp-watch');
var batch = require('gulp-batch');
var cleanCSS = require('gulp-clean-css');
var uglify = require('gulp-uglify');
var concatCss = require('gulp-concat-css');
var uncss = require('gulp-uncss');


gulp.task('css', function() {
    return gulp.src(["./global/css/font-awesome/css/font-awesome.min.css",
            "./global/css/simple-line-icons/simple-line-icons.min.css",
            "./global/css/components.css",
            "./global/css/plugins.css",
            "./global/css/bootstrap-modal-css/bootstrap-modal-bs3patch.css",
            "./global/css/bootstrap-modal-css/bootstrap-modal.css",
            "./global/css/bootstrap-select.min.css"

        ])
        .pipe(concatCss('bootandfriends-styles.min.css'))
        .pipe(cleanCSS())
        .pipe(gulp.dest('./dst'));
});

//returns the css to be combined and packed with css task first
gulp.task('uncss', ['css'], function () {
    return gulp.src('./dst/bootandfriends-styles.min.css')
        .pipe(uncss({
            html: ['index.html']//can also use html string so would need to concat all pject html & tmpl
        }))
        .pipe(gulp.dest('./dst'));
});

gulp.task('build-u', function() {
    return browserify({
            entries: ['./src/test.js']
        })
        .bundle()
        .pipe(source('test.min.js'))
        .pipe(buffer())
        .pipe(uglify({
            compress: true
        }))
        .pipe(gulp.dest('./dst'));

});

gulp.task('build-fb', function() {
    return browserify({
            entries: ['./src/formBuilderPlus']
        })
        .bundle()
        .pipe(source('formbuilderPlus.min.js'))
        .pipe(buffer())
        .pipe(uglify({
            compress: true
        }))
        .pipe(gulp.dest('./dst'));

});


gulp.task('build', function() {
    return browserify({
            entries: ['./src/docusign.js', './src/utils.js']
        })
        .bundle()
        .pipe(source('docusign-bundle.js'))
        .pipe(buffer())
        //.pipe(uglify({compress: true}))
        .pipe(gulp.dest('./dst'));

});

gulp.task('watch', function() {
    return watch('./src/*.js', {
            ignoreInitial: false
        })
        .pipe(gulp.dest('build'));
});

gulp.task('default', ['build']);
