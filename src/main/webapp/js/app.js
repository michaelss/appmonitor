var appmonitor = angular.module('appmonitor', [
  'ngRoute',
  'appmonitorControllers'
]);

appmonitor.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/servers', {
        templateUrl: 'list.html',
        controller: 'ServerListCtrl'
      }).
      when('/servers/:serverId', {
        templateUrl: 'detail.html',
        controller: 'ServerDetailCtrl'
      }).
      otherwise({
        redirectTo: '/servers'
      });
  }]);
