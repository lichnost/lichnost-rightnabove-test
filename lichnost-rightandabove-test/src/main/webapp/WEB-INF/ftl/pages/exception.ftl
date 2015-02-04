<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title><@spring.message "titles.departments"/></title>
	<link rel="stylesheet" href="./../../css/kube.css">
	
	<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.js"></script>
	<script src="./../../js/kube.js"></script>
</head>
<body>

	<div class="label label-red width-100">
		${exception}
	</div>

</body>
</html>