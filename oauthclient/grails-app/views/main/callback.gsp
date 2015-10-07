<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Grails</title>
	</head>
	<body>
		<div id="page-body" role="main">
			<h1>OAuth Test Client</h1>
			<h2>Results from login:</h2>
			<ul>
				<li><label>code:</label> ${code}</li>
				<li><label>state:</label> ${state}</li>
				<li><label>token:</label> ${token}</li>
			</ul>
			
			<g:link controller="main" action="getAccountInfo">Get Account Info</g:link>
			
		</div>
	</body>
</html>
