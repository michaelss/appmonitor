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
          controller: 'ServerNewCtrl'
      }).
      when('/servers/:serverId', {
          templateUrl: 'detail.html',
          controller: 'ServerDetailCtrl'
      }).
      otherwise({
        redirectTo: '/servers'
      });
  }]);
