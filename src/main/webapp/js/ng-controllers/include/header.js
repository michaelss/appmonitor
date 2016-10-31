var module = angular.module('inventory', []);

module.controller('HeaderController', function($scope, $http) {
	
	$scope.username = "mi";
	
	$http.get('/inventory/services/session/username').then(function successCallback(response) {
		if (response.status == 204) {
			alert('There was a problem trying to contact server.');
		}
		else {
			$scope.username = response.data;
		}
	}, function errorCallback(response) { 
		alert('There was a problem trying to contact server.');
	});
});
