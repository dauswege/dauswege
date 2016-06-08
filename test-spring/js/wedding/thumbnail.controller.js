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