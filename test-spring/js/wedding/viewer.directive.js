(function(){
    'use strict';
    
    angular.module('viewerApp')
        .directive('bgImage', bgImage);
    
    function bgImage($window, $timeout) {
        return function(scope, element, attrs) {
            var resizeBG = function() {

                var bgwidth = element.prop('naturalWidth');
                var bgheight = element.prop('naturalHeight');

                var winwidth = $window.innerWidth;
                var winheight = $window.innerHeight;

                var widthratio = winwidth / bgwidth;
                var heightratio = winheight / bgheight;

                var widthdiff = heightratio * bgwidth;
                var heightdiff = widthratio * bgheight;

                if (heightdiff >= winheight) {
                    element.css({
                        height : winheight + 'px',
                        width : widthdiff + 'px'
                    })
                } else {
                    element.css({
                        width : winwidth + 'px',
                        height : heightdiff + 'px'
                    })
                }

               // console.log(element.height());
            };

            var windowElement = angular.element($window);
            windowElement.resize(resizeBG);

            element.bind('load', function() {
                resizeBG();
            });

        };
    };
    
})();