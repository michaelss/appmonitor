module.controller('ListController', function($scope, $http) {
	
	$scope.servers = [];
	
	$scope.loadServers = function() {
		$http.get('/inventory/services/servers').success(function(data) {
			$scope.servers = data;
		});
	};
	
	$scope.loadServers();
	
	$scope.loadApps = function(server){
		if (server.apps || server.loadAppsErrorMessage) {
			server.apps = false;
			server.loadAppsErrorMessage = null;
		}
		else {
			$http.get('/inventory/services/servers/' + server.id + '/apps').then(function successCallback(response) {
				if (response.status == 204) {
					server.loadAppsErrorMessage = 'There was a problem trying to contact server.';
				}
				else {
					server.apps = response.data;
				}
			}, function errorCallback(response) { 
				server.loadAppsErrorMessage = 'There was a problem trying to contact server.';
			});
		}
	};

});
