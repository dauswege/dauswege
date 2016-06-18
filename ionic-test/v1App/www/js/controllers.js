angular.module('starter.controllers', [])

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
        vm.isLoading = true;
        vm.takePhoto(); 
    });

    function onSwipeLeft(){
      $state.go('tab.image');
    };


    function takePhoto () {
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
  };
  
  function choosePhoto () {
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
  };
  
  function setImage(imageData){
    // if (!imageData.startsWith("file://")) {
    //   imageData = "file://" + imageData;
    // }
    Images.set(imageData);
  };
    
});
