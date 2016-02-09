var controllers = angular.module('appmonitorControllers', []);

controllers.controller('ServerListCtrl', ['$scope', '$http',
  function ($scope, $http) {
    $scope.title = 'Server List';
  }]);

controllers.controller('ServerDetailCtrl', ['$scope', '$routeParams',
  function($scope, $routeParams) {
    $scope.serverId = $routeParams.serverId;
  }]);
