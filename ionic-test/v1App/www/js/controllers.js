angular.module('starter.controllers', ['ngResource'])

.controller('ImageController', function($scope, $state, Images){
  var vm = this;
  vm.reject = reject;
  vm.proceed = proceed;
  vm.onSwipeLeft = onSwipeLeft;
  vm.onSwipeRight = onSwipeRight;
  
  vm.imageUri;
  
  $scope.$on('$ionicView.enter', function() {
     vm.imageUri = Images.get();
  });  
  
   function onSwipeLeft(){
    $state.go('tab.mail');
  };
  
  function onSwipeRight(){
    $state.go('tab.photo');
  };
   
  function reject(){
    $state.go('tab.photo');
  };
  
  function proceed(){
    $state.go('tab.mail');
  };
  
})
.controller('MailController', function($scope, $state, $cordovaEmailComposer, Images){
  var vm = this;
  
  vm.sendMail = sendMail;
  
  $scope.$on('$ionicView.enter', function() {
    vm.image = Images.get();
     vm.sendMail(vm.image);
  });  
  
  var email = {
      to: 'w.u.v.hochzeit@gmail.com',
      subject: 'Ein Foto für euch',
      body: '(Du kannst hier Text angeben oder diesen Text stehen lassen)',
      isHtml: true
    };
    
  function sendMail(image){
    var imageData = Images.get();
    email.attachments = [imageData];
    $cordovaEmailComposer.open(email).then(mailSuccess, mailCancel);
  };
  
  function mailSuccess(){
    vm.success = 'Das Foto sollte geschickt worden sein. Vielen Dank dafür!';
  };
  
  function mailCancel(){
    $state.go('tab.photo');
  };
  
})

.controller("CameraController", function ($scope, $state, $ionicPlatform, $cordovaCamera, $cordovaEmailComposer, Images) {
 
    var vm = this;
    
    vm.isLoading = false;
    
    vm.takePhoto = takePhoto;
    vm.choosePhoto = choosePhoto;
    vm.onSwipeLeft = onSwipeLeft;
    
    $ionicPlatform.ready(function(){
        // vm.isLoading = true;
        // vm.takePhoto();  
    });

    function onSwipeLeft(){
      $state.go('tab.image');
    };


    function takePhoto () {
      if(Camera !== undefined){
        
        var options = {
          // quality: 75,
          destinationType: Camera.DestinationType.IMAGE_URI,
          sourceType: Camera.PictureSourceType.CAMERA,
          allowEdit: false,
          encodingType: Camera.EncodingType.JPEG,
          popoverOptions: CameraPopoverOptions,
          correctOrientation: true,
          saveToPhotoAlbum: true
        };

        $cordovaCamera.getPicture(options).then(function (imageData) {
            setImage(imageData);
            vm.isLoading = false;
            $state.go('tab.image');
        }, function (err) {
            vm.isLoading = false;
        });
        
      }
    
  };
  
  function choosePhoto () {
    if(Camera !== undefined){
      var options = {
        //quality: 75,
        destinationType: Camera.DestinationType.FILE_URI,
        sourceType: Camera.PictureSourceType.PHOTOLIBRARY,
        allowEdit: true,
        encodingType: Camera.EncodingType.JPEG,
        popoverOptions: CameraPopoverOptions,
        correctOrientation: false,
        saveToPhotoAlbum: false
      };

      $cordovaCamera.getPicture(options).then(function (imageData) {
          setImage(imageData);
          vm.isLoading = false;
          $state.go('tab.image');
      }, function (err) {
          // An error occured. Show a message to the user
      });
    
    }
  };
  
  function setImage(imageData){
    // if (!imageData.startsWith("file://")) {
    //   imageData = "file://" + imageData;
    // }
    Images.set(imageData);
  };
    
})
.controller('AdminController', function($http, $ionicPlatform, $scope, $ionicModal, $state){
  
  var vm = this;
  var size = 8;
  vm.imageHost = "http://desktop-fvcpl3j:8080"
  
  vm.getThumbs = getThumbs;
  vm.rotateImage = rotateImage;
  vm.deleteImage = deleteImage;
  vm.onSwipeLeft = onSwipeLeft;
  vm.onSwipeRight = onSwipeRight;
  
  vm.page = 1;
  
  $ionicPlatform.ready(function(){
       vm.getThumbs(vm.page); 
    });
  
  function getThumbs(page){
    $http.get(vm.imageHost + "/req/thumbnailsPaged/" + page + "?size=" + size)
    .then(
      function(result){
        vm.thumbs = [];
        // angular.forEach(result.data,function(img){
        //   vm.thumbs.push(img+"?"+new Date());
        // });
        vm.thumbs = result.data;
      }, 
      function(error){
        
      }
    );
  };
  
  function rotateImage(thumb){
    $http.put(vm.imageHost + '/req/rotateImage', {
      'fileName' : thumb
    }).then(
      function(result){
        vm.getThumbs(vm.page);
        $state.go($state.current, {}, {reload:true});
      }, 
      function(error){}
    )
  };
  
  function deleteImage(thumb){
    $http.put(vm.imageHost + '/req/deleteImage', {
      'fileName' : thumb
    }).then(
      function(result){
        vm.getThumbs(vm.page);
        $state.go($state.current, {}, {reload:true});
      }, 
      function(error){}
    )
  };
  
  function onSwipeLeft(){
    vm.page++;
    vm.getThumbs(vm.page);
    if(vm.thumbs == undefined || vm.thumbs.length == 0){
      vm.page--;
    }
  };
  
  function onSwipeRight(){
    if(vm.page > 1){
      vm.page--;
      vm.getThumbs(vm.page);
    }
  };
  
  // MODAL
  $ionicModal.fromTemplateUrl('my-modal.html', {
    scope: $scope,
    animation: 'slide-in-up'
  }).then(function(modal) {
    vm.modal = modal;
  });
  vm.openModal = function(thumb) {
    vm.currentThumb = thumb;
    vm.modal.show();
  };
  vm.closeModal = function() {
    vm.modal.hide();
  };
  // Cleanup the modal when we're done with it!
  $scope.$on('$destroy', function() {
    vm.modal.remove();
  });
  // Execute action on hide modal
  $scope.$on('modal.hidden', function() {
    // Execute action
  });
  // Execute action on remove modal
  $scope.$on('modal.removed', function() {
    // Execute action
  });
  
})
;
