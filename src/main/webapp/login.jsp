<%@include file="include/header.jsp" %>

	<div ng-controller="LoginController">
	
		<div class="row column">
			<br>
		</div>
		
		<div class="row">
			<div class="medium-offset-3 medium-6 small-10 small-offset-1 columns">
				<div class="callout">
					<form action="" method="post" accept-charset="UTF-8">
						<div class="row ">
							<div class="small-12 medium-offset-2 medium-8 columns">
								<h1>Login</h1>
							</div>
						</div>
						<div class="row">
							<div class="small-12 medium-offset-2 medium-8 columns">
								<label>Username <input type="text"
									name="username" required></input>
								</label>
							</div>
						</div>
						<div class="row">
							<div class="small-12 medium-offset-2 medium-8 columns">
								<label>Password <input type="password"
									name="password" required></input>
								</label>
							</div>
						</div>
						<div class="row">
							<div class="small-12 medium-offset-2 medium-8 columns">
								<button type="submit" class="button">Submit</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	
	</div>

	<div class="row column">
		<hr>
	</div>

	<%@include file="include/footer.jsp" %>
	<script src="js/ng-controllers/login.js"></script>
</body>
</html>