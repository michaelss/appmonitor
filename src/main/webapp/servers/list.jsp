<%@include file="/include/header.jsp" %>

	<div ng-controller="ListController">

		<br>
		<div class="row" ng-init="loadServers()">
			<div class="small-9 columns">
				<h1>Servers and apps</h1>
				<p class="subheader">Here you can view the servers and their
					deployed apps.</p>
			</div>
			<div class="small-3 columns">
				<shiro:user>
					<a href="#/servers/new" class="button hollow float-right"><i
						class="fi-plus"></i> Add server</a>
				</shiro:user>
			</div>
		</div>

		<div class="row column">
			<hr>
		</div>
	
		<div class="row small-up-1 medium-up-2 large-up-3">
			<div class="column" ng-repeat="server in servers">
				<div class="callout">
					<p class="lead">{{server.host}}</p>
					<p class="subheader">{{server.description}}</p>
					<p>
						<span><a
							ng-show="!server.apps && !server.loadAppsErrorMessage"
							ng-click="loadApps(server)"> <i class="fi-list"></i> View apps
						</a></span> <span ng-show="server.apps || server.loadAppsErrorMessage"><a
							ng-click="loadApps(server)"> <i class="fi-list"></i> Hide apps
						</a></span> <span ng-if="userIsLogged()" class="float-right"><a
							href="#/servers/edit/{{server.id}}"><i class="fi-pencil"></i>
								Edit</a> </span>
					</p>
					<p style="color: red;" ng-show="server.loadAppsErrorMessage">{{server.loadAppsErrorMessage}}</p>
					<table style="width: 100%;" ng-show="server.apps">
						<thead>
							<tr>
								<th>App</th>
								<th>Enabled?</th>
							</tr>
						</thead>
						<tbody>
							<tr ng-repeat="app in server.apps">
								<td>{{app.name}}</td>
								<td>{{app.active}}</td>
							</tr>
						</tbody>
					</table>
					<!-- a class="button hollow">Edit</a-->
				</div>
			</div>
		</div>

	</div>

	<div class="row column">
		<hr>
	</div>
	
	<%@include file="/include/footer.jsp" %>
	<script src="${pageContext.request.contextPath}/js/ng-controllers/servers/list.js"></script>
</body>
</html>