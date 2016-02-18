var module = angular.module('appmonitorControllers', []);

module.controller('ApplicationCtrl', ['$scope', '$rootScope', '$location', '$http',
                                      
        function($scope, $rootScope, $location, $http) {
	
			$scope.username = sessionStorage['username'];
	
			$scope.setUsername = function(username) {
				$scope.username = sessionStorage['username'] = username;
			};
			
			$scope.userIsLogged = function() {
				return !!$scope.username;
			};
			
			$scope.logout = function() {
				$scope.username = null;
				delete sessionStorage['username'];
				$http.get('services/users/invalidate');
				$location.path('/servers');
			}
			
			$rootScope.$on("$routeChangeStart", function(event, next, current) {
				if (next.$$route !== undefined && next.$$route.originalPath != '/login') {
					$scope.verifySession();
				}
			});
			
			$scope.verifySession = function() {
				$http.get('services/users/isAuthorized/' + $scope.username).then(function successCallback(response) {
				}, function errorCallback(response) {
					$scope.logout();
					$location.path('/servers');
				});
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

module.controller('LoginCtrl', [ '$scope', '$location', '$http',
		function($scope, $location, $http) {
	
			$scope.form = {};
	
			$scope.login = function() {
				
				$http.post('services/users/authenticate', $scope.form).then(function successCallback(response) {
					$scope.setUsername($scope.form.username);
					$location.path('/servers');
				}, function errorCallback(response) { 
					// TODO: find a good way to show error messages in the same page.
					// $scope.localMessage
				});
			};
		} ]);