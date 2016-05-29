var gulp = require('gulp');



gulp.task('default', function() {
  // place code for your default task here
  return gulp.src('node-modules/bootstrap/dist/css/bootstrap.min.css')
  .pipe(gulp.dest('src/main/resources/bootstrap.min.css'));
});