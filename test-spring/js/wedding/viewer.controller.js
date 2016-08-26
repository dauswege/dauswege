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
        
        vm.nextPic = nextPic;
        
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
        
        function nextPic() {
            changeInterval(10000);
            getImage();
        };

    };
    
})();