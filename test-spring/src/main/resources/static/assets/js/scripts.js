(function(){
    'use strict';
    
    angular.module('testapp',[]);
    
})();
(function(){
    'use strict';
    
    angular.module('testapp')
    .controller('TestController', testController);
    
    testController.$inject = ['$http'];
    
    function testController($http){
        
        var self = this;
     
        this.teststring = 'this is a test string';
        getTestData();
        function getTestData (){
          $http.get(BASEURL + 'api/test').then(function(result){
      
            });  
        };
        
        
    }; 
    
})();
/* global EXIF */
(function(){
    

    'use strict';

    // Require library: https://github.com/jseidelin/exif-js
    // Require Jquery (If not have jquery you must handle DOM by native js code)
    /**
     @description this directive auto rotate image by css base on image orientation value
    Check the example of image orientation here: 
        https://github.com/recurser/exif-orientation-examples
    @example
        <div class="modal-upload-file-thumbnail" ng-if="isOneImageFile">
        <img ng-src="{{thumbnail}}" img-orientation/>
        </div>
    **/

    var imgDirs = angular.module('imageDirs', []);
    imgDirs.directive('imgOrientation', imgOrientation);
    
    function imgOrientation(){
        return {
            restrict: 'A',
            link: function(scope, element/*, attrs*/) {
            function setTransform(transform) {
                element.css('-ms-transform', transform);
                element.css('-webkit-transform', transform);
                element.css('-moz-transform', transform);
                element.css('transform', transform);
            }

            var parent = element.parent();
            $(element).bind('load', function() {
                EXIF.getData(element[0], function() {
                var orientation = EXIF.getTag(element[0], 'Orientation');
                var height = element.height();
                var width = element.width();
                if (orientation && orientation !== 1) {
                    switch (orientation) {
                    case 2:
                        setTransform('rotateY(180deg)');
                        break;
                    case 3:
                        setTransform('rotate(180deg)');
                        break;
                    case 4:
                        setTransform('rotateX(180deg)');
                        break;
                    case 5:
                        setTransform('rotateZ(90deg) rotateX(180deg)');
                        if (width > height) {
                        parent.css('height', width + 'px');
                        element.css('margin-top', ((width -height) / 2) + 'px');
                        }
                        break;
                    case 6:
                        setTransform('rotate(90deg)');
                        if (width > height) {
                        parent.css('height', width + 'px');
                        element.css('margin-top', ((width -height) / 2) + 'px');
                        }
                        break;
                    case 7:
                        setTransform('rotateZ(90deg) rotateY(180deg)');
                        if (width > height) {
                        parent.css('height', width + 'px');
                        element.css('margin-top', ((width -height) / 2) + 'px');
                        }
                        break;
                    case 8:
                        setTransform('rotate(-90deg)');
                        if (width > height) {
                        parent.css('height', width + 'px');
                        element.css('margin-top', ((width -height) / 2) + 'px');
                        }
                        break;
                    }
                }
                });
            });
            }
        };
        };

})();
(function(){
    'use strict';
    
    angular.module('thumbnailApp',[]);
    
})();
(function() {
    'use strict';
    
    angular.module('thumbnailApp')
        .controller('ThumbnailController', thumbnailController);
    
    thumbnailController.$inject = ['$http'];
    
    function thumbnailController($http){
        var vm = this;
        
        vm.test = "testasdf";
        
    };
    
})();
(function(){
    'use strict';
    
    angular.module('viewerApp',['imageDirs']);
    
})();
(function() {
    'use strict';
    
    angular.module('viewerApp')
        .controller('ViewerController', viewerController);
    
    viewerController.$inject = ['$http', '$interval'];
    
    function viewerController($http, $interval){
        var vm = this;
        
        vm.getImage = getImage;
        vm.changeInterval = changeInterval;
        vm.changeInterval(10000);
        vm.getImage();
        
        vm.test = 'asd';
        vm.interval;

        function getImage() {
            $http.get(BASEURL + 'req/pic').success(function(data) {
                vm.image = BASEURL + data;
            });

        };

        function changeInterval(intervalTime) {
            if (intervalTime < 1000) {
                intervalTime = intervalTime * 1000;
            }
            vm.interval = $interval.cancel(vm.interval);
            vm.interval = $interval(getImage, intervalTime);
            return vm.interval;
        };

    };
    
})();
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