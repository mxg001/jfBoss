<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html ng-app="inspinia">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	<sec:csrfMetaTags />
	<meta HTTP-EQUIV="pragma" CONTENT="no-cache"> 
	<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
	<meta HTTP-EQUIV="expires" CONTENT="0"> 
    <!-- Page title set in pageTitle directive -->
    <title page-title></title>
    <link rel="icon" type="image/x-icon" href="favicon.ico">
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Font awesome -->
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet">

    <!-- Main Inspinia CSS files -->
    <link href="css/animate.css" rel="stylesheet">
    <link href="css/plugins/ui-grid/ui-grid.css" rel="stylesheet">
    <!-- Main global notify -->
    <link href="css/plugins/angular-notify/angular-notify.min.css" rel="stylesheet">
    <link id="loadBefore" href="css/style.css" rel="stylesheet">

	<script type="text/javascript">
		var _token="${_csrf.token}";
		var _parameterName="${_csrf.parameterName}";
		var _principal = ${principalJSON};
		var _permits = ${permitsJSON};
		var verNo = "${verNo}";
	</script>
</head>

<!-- ControllerAs syntax -->
<!-- Main controller with serveral data used in Inspinia theme on diferent view -->
<body ng-controller="MainCtrl as main" class="{{$state.current.data.specialClass}}" landing-scrollspy id="page-top">

<!-- Main view  -->
<div ui-view></div>

<!-- jQuery and Bootstrap -->
<script src="js/jquery/jquery-2.1.1.min.js"></script>
<script src="js/plugins/jquery-ui/jquery-ui.js"></script>
<script src="js/bootstrap/bootstrap.min.js"></script>

<!-- MetsiMenu -->
<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>

<!-- SlimScroll -->
<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- Peace JS -->
<script src="js/plugins/pace/pace.min.js"></script>

<!-- Custom and plugin javascript -->
<script src="js/inspinia.js"></script>

<!-- Main Angular scripts-->
<script src="js/angular/angular.min.js"></script>
<script src="js/angular/angular-sanitize.js"></script>
<script src="js/plugins/oclazyload/dist/ocLazyLoad.min.js"></script>
<script src="js/ui-router/angular-ui-router.min.js"></script>
<script src="js/bootstrap/ui-bootstrap-tpls-1.1.2.min.js"></script>
<script src="js/plugins/angular-idle/angular-idle.js"></script>
<script src="js/plugins/ui-grid/ui-grid.js"></script>
<script src="js/plugins/angular-notify/angular-notify.min.js"></script>
<script src="js/plugins/moment/moment.min.js"></script>

<!-- Anglar App Script -->
<script src="js/states/system.js"></script>
<script src="js/states/stateInit.js"></script>
<script src="js/states/integral.js"></script>

<script src="js/app.js"></script>
<script src="js/config.js"></script>
<script src="js/directives.js"></script>
<script src="js/controllers.js"></script>

</body>
</html>
