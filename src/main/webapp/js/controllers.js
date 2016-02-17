var module = angular.module('appmonitorControllers', []);

module.controller('ApplicationCtrl', ['$scope', '$location', 'userService',
                                      
        function($scope, $location, userService) {
	
			$scope.$on('user.logged', function(event) {
				$scope.username = sessionStorage['username'];
			});
	
			$scope.$on('user.logout', function(event) {
				$scope.username = sessionStorage['username'];
			});
			
			$scope.username = sessionStorage['username'];
			
			$scope.userIsLogged = function() {
				return !!$scope.username;
			};
			
			$scope.logout = function() {
				userService.logout();
				$location.path('/servers');
			}
			
		} ]);

module.controller('ServersCtrl', [ '$scope', '$http', '$location', 'flash', '$routeParams', 'userService',
		function($scope, $http, $location, flash, $routeParams, userService) {

			$scope.servers = [];
			
			$scope.flash = flash;
			
			$scope.form = {};
	
			$scope.loadServers = function() {
				$http.get('services/servers').success(function(data) {
					$scope.servers = data;
				});
			};
			
			$scope.loadApps = function(server){
				if (server.apps || server.loadAppsErrorMessage) {
					server.apps = false;
					server.loadAppsErrorMessage = null;
				}
				else {
					$http.get('services/servers/' + server.id + '/apps').then(function successCallback(response) {
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

			$scope.addServer = function() {
				$http.post('services/servers/', $scope.form).then(function successCallback(response) {
					flash.setMessage({'text': 'The server was added.', 'status': 'success'});
					$location.path('/servers');
				}, function errorCallback(response) { 
					console.log('erro.');
				});
			};
			
			$scope.loadServer = function() {
				$http.get('services/servers/' + $routeParams.serverId).then(function successCallback(response) {
					if (response.status == 204) {
						flash.setMessage({'text': 'The server was not found.', 'status': 'alert'});
						$location.path('/servers');
					}
					else {
						$scope.form = response.data;
					}
				}, function errorCallback(response) { 
					flash.setMessage({'text': 'There was a problem loading the server.', 'status': 'alert'});
					$location.path('/servers');
				});
			};

			$scope.editServer = function() {
				$http.post('services/servers/edit', $scope.form).then(function successCallback(response) {
					flash.setMessage({'text': 'The server information was updated.', 'status': 'success'});
					$location.path('/servers');
				}, function errorCallback(response) { 
					// TODO: find a good way to show error messages in the same page.
					// $scope.localMessage
				});
			};
			
			$scope.removeServer = function() {
				$http.post('services/servers/remove', $scope.form.id).then(function successCallback(response) {
					flash.setMessage({'text': 'The server was removed.', 'status': 'success'});
					$location.path('/servers');
				}, function errorCallback(response) { 
					console.log('erro.');
				});
			}
		
		} ]);

module.controller('LoginCtrl', [ '$scope', '$location', 'userService',
		function($scope, $location, userService) {
	
			$scope.form = {};
	
			$scope.login = function() {
				userService.setUser($scope.form.username);
//				$scope.setUsername($scope.form.username);
//				sessionStorage['username'] = $scope.form.username; 
				$location.path('/servers');
				
//				$http.post('services/login', $scope.form).then(function successCallback(response) {
//					$location.path('/servers');
//				}, function errorCallback(response) { 
//					// TODO: find a good way to show error messages in the same page.
//					// $scope.localMessage
//				});
			};
		} ]);