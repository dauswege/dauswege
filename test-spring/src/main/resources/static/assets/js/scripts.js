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
(function(){
    'use strict';
    
    angular.module('viewerApp',[])
    
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