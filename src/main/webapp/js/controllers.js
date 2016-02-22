var module = angular.module('appmonitorControllers', []);

module.constant('messageDelay', 5000);

module.controller('ApplicationCtrl',
        function($scope, $rootScope, $location, $http) {
	
			$scope.username = sessionStorage['username'];
			
			$scope.message = '';
			
			$scope.setMessage = function(message) {
				$scope.message = message;
			}
	
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
				if (next.$$route !== undefined && next.$$route.originalPath != '/users/login'
					&& next.$$route.originalPath != '/servers') {
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
			
		});

module.controller('ServersCtrl',
		function($scope, $http, $location, flash, $routeParams) {

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
					$scope.setMessage({'text': 'Error editing server information.', 'status': 'alert'});
					$timeout(function() { $scope.setMessage('')}, 5000);
				});
			};
			
			$scope.removeServer = function() {
				$http.post('services/servers/remove', $scope.form.id).then(function successCallback(response) {
					flash.setMessage({'text': 'The server was removed.', 'status': 'success'});
					$location.path('/servers');
				}, function errorCallback(response) { 
					$scope.setMessage({'text': 'Error removing server.', 'status': 'alert'});
					$timeout(function() { $scope.setMessage('')}, 5000);
				});
			}
		
		});

module.controller('UsersCtrl',
		function($scope, $location, $http, $timeout, $routeParams, messageDelay, flash) {
	
			$scope.flash = flash;
	
			$scope.users = [];
	
			$scope.form = {};
	
			$scope.login = function() {
				$http({method: 'POST', 
					url: 'services/users/authenticate', 
					data: $.param($scope.form),
					headers: {'Content-Type': 'application/x-www-form-urlencoded'}
				}).then(function successCallback(response) {
					$scope.setUsername($scope.form.username);
					$location.path('/servers');
				}, function errorCallback(response) {
					$scope.setMessage({'text': 'Wrong username or password.', 'status': 'alert'});
					$timeout(function() { $scope.setMessage('')}, messageDelay);
				});
			};
			
			$scope.loadUsers = function() {
				$http.get('services/users').then(function successCallback(response) {
					$scope.users = response.data;
				});
			};
			
			$scope.loadUser = function() {
				$http.get('services/users/' + $routeParams.userId).then(function successCallback(response) {
					if (response.status == 204) {
						flash.setMessage({'text': 'The user was not found.', 'status': 'alert'});
						$location.path('/users');
					}
					else {
						$scope.form = response.data;
					}
				}, function errorCallback(response) { 
					flash.setMessage({'text': 'There was a problem loading the user.', 'status': 'alert'});
					$location.path('/users');
				});
			};
			
			$scope.editUser = function() {
				$http.post('services/users/edit', $scope.form).then(function successCallback(response) {
					flash.setMessage({'text': 'The user information was updated.', 'status': 'success'});
					$location.path('/users');
				}, function errorCallback(response) { 
					$scope.setMessage({'text': 'Error editing user information.', 'status': 'alert'});
					$timeout(function() { $scope.setMessage('')}, 5000);
				});
			};
			
			$scope.addUser = function() {
				$http.post('services/users', $scope.form).then(function successCallback(response) {
					flash.setMessage({'text': 'The user was added.', 'status': 'success'});
					$location.path('/users');
				}, function errorCallback(response) { 
					$scope.setMessage({'text': 'Error creating user.', 'status': 'alert'});
					$timeout(function() { $scope.setMessage('')}, 5000);
				});
			};

			$scope.removeUser = function() {
				$http.post('services/users/remove', $scope.form.id).then(function successCallback(response) {
					flash.setMessage({'text': 'The user was removed.', 'status': 'success'});
					$location.path('/users');
				}, function errorCallback(response) { 
					$scope.setMessage({'text': 'Error removing user.', 'status': 'alert'});
					$timeout(function() { $scope.setMessage('')}, 5000);
				});
			}
		});