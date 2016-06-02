var gulp = require('gulp');
    concatCss = require('gulp-concat-css');
    csslint = require('gulp-csslint');
    concat = require('gulp-concat'),
    watch = require('gulp-watch'),
    ts = require('gulp-typescript');
    //angularFilesort = require('gulp-angular-filesort'),
    //inject = require('gulp-inject')
    ;

gulp.task('watch', function(){
  return gulp.watch('js/**/*.js', ['scripts'])
  ;
});

gulp.task('ts', function(){
  return gulp.src('typings/**/*.ts')
    .pipe(ts({
      noImplicitAny: true,
      out: 'tscripts.js'
    }))
    .pipe(gulp.dest('src/main/resources/static/assets/js/'));
})

gulp.task('scripts', function(){
  return gulp.src(['js/**/*.js'])
    .pipe(concat('/scripts.js'))
    .pipe(gulp.dest('src/main/resources/static/assets/js/'));
});

gulp.task('vendorjs', function(){
  return gulp.src(['node_modules/angular/angular.min.js', 
                   'node_modules/bootstrap/dist/js/bootstrap.min.js'           
     ])
    .pipe(gulp.dest('src/main/resources/static/assets/js/'));
});

gulp.task('vendorcss', function(){
  return gulp.src('node_modules/bootstrap/dist/css/*.css')
  .pipe(concatCss("/vendor.css"))
  .pipe(gulp.dest('src/main/resources/static/assets/style'));
});

gulp.task('default', ['vendorcss', 'vendorjs', 'scripts', 'ts']);