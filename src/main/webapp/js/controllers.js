var controllers = angular.module('appmonitorControllers', []);

controllers.controller('ServersCtrl', [ '$scope', '$http',
		function($scope, $http) {
			$scope.servers = [];
			$http.get('services/servers').success(function(data) {
				$scope.servers = data;
			});
			
			$scope.fn = function(param){
				alert('Fui clicado...' + param);
			};
			$scope.loadApps = function(server){
				if (server.apps == null) {
					$http.get('services/servers/' + server.id + '/apps').success(function (response) { //then(function successCallback(response) {
						server.apps = response;
//						server.viewHideAppsLabel = (server.visible) ? "Hide apps" : "View apps";
					});
				}
				server.visible = !server.visible;
			};
		} ]);

controllers.controller('ServerDetailCtrl', [ '$scope', '$routeParams',
		function($scope, $routeParams) {
			$scope.serverId = $routeParams.serverId;
		} ]);

controllers.controller('ServersNewCtrl', [ '$scope', '$http',
		function($scope, $http) {
			$scope.form = {};
	
			$scope.addServer = function() {
				$http.post('services/servers/', $scope.form).then(function successCallback(response) {
					console.log('inserido.');
				}, function errorCallback(response) { 
					console.log('erro.');
				});
			};
		}]);
