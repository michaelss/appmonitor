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
				$http.get('services/servers/' + server.id + '/apps').success(function (response) { //then(function successCallback(response) {
					server.apps = response;
					server.visible = !server.visible;
//					server.viewHideAppsLabel = (server.visible) ? "Hide apps" : "View apps";
				});
			};
		} ]);

controllers.controller('ServerDetailCtrl', [ '$scope', '$routeParams',
		function($scope, $routeParams) {
			$scope.serverId = $routeParams.serverId;
		} ]);

controllers.controller('ServersNewCtrl', [ '$scope', '$routeParams',
		function($scope, $routeParams) {
			$scope.serverId = $routeParams.serverId;
		} ]);
