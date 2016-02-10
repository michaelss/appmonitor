var controllers = angular.module('appmonitorControllers', []);

controllers.controller('ServersCtrl', [ '$scope', '$http',
		function($scope, $http) {
			var serverList = this;
			
			$http.get('services/servers/1/apps').success(function(data) {
				serverList.apps = data;
			});
//			serverList.apps = [{"name":"appmonitor.war","active":false},
//			                {"name":"padlog.war","active":true}]; 
		} ]);

controllers.controller('ServerDetailCtrl', [ '$scope', '$routeParams',
		function($scope, $routeParams) {
			$scope.serverId = $routeParams.serverId;
		} ]);

controllers.controller('ServersNewCtrl', [ '$scope', '$routeParams',
		function($scope, $routeParams) {
			$scope.serverId = $routeParams.serverId;
		} ]);
