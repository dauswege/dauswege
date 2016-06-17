angular.module('starter.services', [])
.factory('Images', function(){
  var imageUrl;
  
  return {
    get: function(){
      return this.imageUrl;
    },
    set: function(imgUrl){
      this.imageUrl = imgUrl;
    }
  }
});
