<%@include file="/include/header.jsp" %>

	<div ng-controller="ListController">

	{{titulo}}
	
		<div class="row column">
			<br>
		</div>
	
	</div>

	<div class="row column">
		<hr>
	</div>

	<%@include file="/include/footer.jsp" %>
	<script src="${pageContext.request.contextPath}/js/ng-controllers/servers/list.js"></script>
</body>
</html>