var gulp = require('gulp');
var browserify = require('browserify');
var source = require('vinyl-source-stream');
var buffer = require('gulp-buffer');
var watch = require('gulp-watch');
var batch = require('gulp-batch');
var cleanCSS = require('gulp-clean-css');
var uglify = require('gulp-uglify');
var concatCss = require('gulp-concat-css');
//var uncss = require('gulp-uncss');

// gulp.task('css', function() {
//     return gulp.src(["./global/css/font-awesome/css/font-awesome.min.css",
//             "./global/css/simple-line-icons/simple-line-icons.min.css",
//             "./global/css/components.css",
//             "./global/css/plugins.css",
//             "./global/css/bootstrap-modal-css/bootstrap-modal-bs3patch.css",
//             "./global/css/bootstrap-modal-css/bootstrap-modal.css",
//             "./global/css/bootstrap-select.min.css",
//             "./global/css/suggest.css"
//
//         ])
//         .pipe(concatCss('docusign-styles.min.css'))
//         .pipe(cleanCSS())
//         .pipe(gulp.dest('./dst'));
// });


//returns the css to be combined and packed with css task first
////can also use html string so would need to concat all pject html & tmpl
gulp.task('css', ['css-boots'], function () {
    return gulp.src([
            "./global/css/font-awesome/css/font-awesome.min.css",
            "./global/css/simple-line-icons/simple-line-icons.min.css",
            "./global/css/components.css",
            "./global/css/plugins.css",
            "./global/css/suggest.css"


        ])
        .pipe(concatCss('docusign-styles.min.css'))
  //       .pipe(uncss({
  //           html: ['../../myHome.html'
  //           ,'./templates/embeddedViewDocSend.tmpl.html'
  // ,'./templates/envelopeSummary.tmpl.html'
  // ,'./templates/modal.tmpl.html'
  // ,'./templates/dataTables.tmpl.html'
  // ,'./templates/setupDocumentForm.tmpl.html'
  // ,'./templates/addUserToAccountWrapper.tmpl.html'
  // ,'./templates/disableUserFormWrapper.tmpl.html'
  // ,'./templates/signAndSendModalBody.tmpl.html'
  // ,'./templates/formBuilder.bp.html']
  //       }))
        .pipe(cleanCSS())
        .pipe(gulp.dest('dst'));
});

gulp.task('css-boots', function () {
    return gulp.src([
            "./global/bootstrap/css/bootstrap.css",
            "./global/css/dataTables.bootstrap.min.css",
            "./global/css/bootstrap-dialog.min.css"

        ])
        .pipe(concatCss('docusign-boot-styles.min.css'))
  //       .pipe(uncss({
  //           html: ['../../myHome.html'
  //           ,'./templates/embeddedViewDocSend.tmpl.html'
  // ,'./templates/envelopeSummary.tmpl.html'
  // ,'./templates/modal.tmpl.html'
  // ,'./templates/dataTables.tmpl.html'
  // ,'./templates/setupDocumentForm.tmpl.html'
  // ,'./templates/addUserToAccountWrapper.tmpl.html'
  // ,'./templates/disableUserFormWrapper.tmpl.html'
  // ,'./templates/signAndSendModalBody.tmpl.html'
  // ,'./templates/formBuilder.bp.html',
  // './templates/ref/bootstrap-css-reduction-content.html']
  //       }))
        //.pipe(cleanCSS()) // done in lessc for -iso with arg -x
        .pipe(gulp.dest('dst'));
});

gulp.task('build-fb', function() {
    return browserify({
            entries: ['./src/formBuilderPlus']
        })
        .bundle()
        .pipe(source('formbuilderPlus.min.js'))
        .pipe(buffer())
        // .pipe(uglify({
        //     compress: true
        // }))
        .pipe(gulp.dest('./dst'));

});

gulp.task('build', function() {
    return browserify({
            entries: ['./src/docusign.js']
        })
        .bundle()
        .pipe(source('docusign-bundle.js'))
        .pipe(buffer())
        .pipe(uglify({compress: true}))
        .pipe(gulp.dest('./dst'));

});

gulp.task('watch', function() {
    return watch('./src/docusign.js', {
            ignoreInitial: false
        })
        .pipe(gulp.dest('build'));
});

gulp.task('default', ['build']);
