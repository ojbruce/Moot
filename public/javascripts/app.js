var moot = angular.module('moot', []);

function MootCtrl($scope ) {

    // The logged user
    $scope.loggedUser={};

    // Max text size
    $scope.maxTextLength = 144;
    $scope.text = "";
    $scope.message={};

    //Element
    $scope.users = {};
    $scope.messages = [];
    $scope.feed = "my_feed"
    $scope.feed_page = {'my_feed': 0, 'global_feed': 0};



    //map of user_id => object describing public components of user
    $scope.init = function (uid, username) {
        $scope.loggedUser.uid = uid;
        $scope.loggedUser.username = "Lola";
        //$scope.users[current_user] = {'uid':current_user, 'username': username, 'isFollowing':false};
    }

    /**
    * To post a message
    */
    $scope.post = function(){
        $scope.message.pid = guid();
        $scope.message.body = $scope.text;
        $scope.message.date = new Date().getTime();
        $scope.message.author = $scope.loggedUser.username;


        if($scope.text.length < $scope.maxTextLength && $scope.text.length > 0 ){
            $scope.messages.push($scope.message);
            $scope.text = "";
            ///flush id
            $scope.message={};

            //TODO : Pousser to the server
        }
        
        //Le stocker dans les donnees
        // Envoyer le poste au serveur

    };

    function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }

  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
    s4() + '-' + s4() + s4() + s4();
}
   

    //var global_feed_messages = new StringSet();
    //var my_feed_messages = new StringSet();




    $scope.followUser = function(user_id) {
         
          $http({ method: 'GET', url: '/user/follow/' + user_id }).
              success(function(data, status, headers, config) {
                    console.log("followed user " + user_id + ", " + JSON.stringify(data));
                    $scope.users[user_id].isFollowing = true;
              }).
              error(function(data, status, headers, config) {
                    console.log("failed to follow user " + user_id + ", " + status);
          });
    }

    $scope.unfollowUser = function(user_id) {
       
        $http({ method: 'GET', url: '/user/unfollow/' + user_id }).
            success(function(data, status, headers, config) {
                  console.log("unfollowed user " + user_id + ", " + JSON.stringify(data));
                  $scope.users[user_id].isFollowing  = false;
            }).
            error(function(data, status, headers, config) {
                  console.log("failed to unfollow user " + user_id + ", " + status);
        });
  }
}