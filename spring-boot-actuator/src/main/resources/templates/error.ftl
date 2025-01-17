<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html>
<head>
	<title>Error</title>
    <#assign home><@spring.url relativeUrl="/"/></#assign>
    <#assign bootstrap><@spring.url relativeUrl="/css/bootstrap.min.css"/></#assign>
	<link rel="stylesheet" href="${bootstrap}" />
</head>
<body>
<div class="container">
	<div class="row clearfix">
		<div class="col-md-12 column">
			<nav class="navbar navbar-default" role="navigation">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse"
									data-target="#bs-example-navbar-collapse-1"><span class="sr-only">Toggle navigation</span><span
								class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="http://spring.io/"> Spring </a>
				</div>

				<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav">
						<li class="active">
							<a class="brand" href="http://getbootstrap.com/"> Bootstrap </a>
						</li>
						<li>
							<a class="brand" href="http://freemarker.org/"> FreeMarker </a>
						</li>
					</ul>
					<form class="navbar-form navbar-left" role="search">
						<div class="form-group">
							<input type="text" class="form-control" />
						</div>
						<button type="submit" class="btn btn-default">Submit</button>
					</form>
					<ul class="nav navbar-nav navbar-right">
						<li>
							<a href="https://github.com/dunwu/">Github</a>
						</li>
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown<strong
										class="caret"></strong></a>
							<ul class="dropdown-menu">
								<li>
									<a href="#">Action</a>
								</li>
								<li>
									<a href="#">Another action</a>
								</li>
								<li>
									<a href="#">Something else here</a>
								</li>
								<li class="divider">
								</li>
								<li>
									<a href="#">Separated link</a>
								</li>
							</ul>
						</li>
					</ul>
				</div>

			</nav>
			<div class="jumbotron">
				<h1>Error Page</h1>
				<div id="created">${timestamp?datetime}</div>
				<div>
					There was an unexpected error (type=${error}, status=${status}).
				</div>
				<div>${message}</div>
				<div>
					Please contact the operator with the above information.
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
