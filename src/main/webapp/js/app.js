$(document).foundation();

var appmonitor = angular.module('appmonitor', [
  'ngRoute',
  'appmonitorControllers'
]);

appmonitor.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/servers', {
        templateUrl: 'servers/list.html',
        controller: 'ServersCtrl'
      }).
      when('/servers/new', {
          templateUrl: 'servers/new.html',
          controller: 'ServersCtrl'
      }).
      when('/servers/edit/:serverId', {
          templateUrl: 'servers/edit.html',
          controller: 'ServersCtrl'
      }).
      when('/login', {
          templateUrl: 'login.html',
          controller: 'LoginCtrl',
          access: { isFree: true }
      }).
      otherwise({
        redirectTo: '/servers'
      });
  }]);

appmonitor.factory("flash", function($rootScope) {
	  var queue = [];
	  var currentMessage = "";

	  $rootScope.$on("$routeChangeSuccess", function() {
	    currentMessage = queue.shift() || "";
	  });

	  return {
	    setMessage: function(message) {
	      queue.push(message);
	    },
	    getMessage: function() {
	      return currentMessage;
	    }
	  };
	});

appmonitor.factory('userService', [function() {
	  var sdo = {
	    isLogged: false,
	    username: ''
	  };
	  return sdo;
	}]);