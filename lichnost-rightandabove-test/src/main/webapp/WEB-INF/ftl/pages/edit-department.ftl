<#import "/spring.ftl" as spring />
<@spring.bind "model" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title><@spring.message "titles.edit-department"/></title>
	<link rel="stylesheet" href="./../../css/kube.css">
	
	<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.js"></script>
	<script src="./../../js/kube.js"></script>
</head>
<body>
<#if spring.status.error>
	<div class="tools-message tools-message-red">
		<ul>
			<#list spring.status.errors.globalErrors as error>
			<li>${error.defaultMessage}</li>   
			</#list>
		</ul>
	</div>
</#if>

<form class="forms" style="margin: 30px;" action="../save/department.html" method="post">
	<#if model.department??>
		<input name="id" type="text" class="hide" style="display: none;" value="${model.department.id}"/>
	</#if>
	<label>
		<@spring.message "department.name"/>
		<#if model.binderrors?? && model.binderrors.hasFieldErrors("name")>
			<span class="error"><@spring.message model.binderrors.getFieldError("name").getCode()/></span>
		</#if>
		<input name="name" type="text" class="width-100"
			<#if model.department??>
				value="${model.department.name}"
			</#if>
			<#if model.viewOnly>
				readonly
			</#if>
		/>
	</label>
	
	<div class="group right">
		<#if !model.viewOnly>
			<input type="submit" class="btn btn-blue" value="<@spring.message "edit.button.save"/>" />
		</#if>
		<a href="../departments.html" class="btn"><@spring.message "edit.button.cancel"/></a>
	</div>
</form>

</body>
</html>