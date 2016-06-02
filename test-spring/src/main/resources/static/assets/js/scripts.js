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