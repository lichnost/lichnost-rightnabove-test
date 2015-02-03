<#import "/spring.ftl" as spring />
<@spring.bind "model" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title><@spring.message "titles.edit-employee"/></title>
	<link rel="stylesheet" href="./../../css/kube.css">
	
	<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.js"></script>
	<script src="./../../js/kube.js"></script>
	<script>
		
	</script>
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

<form class="forms" style="margin: 30px;" action="../save/employee.html" method="post">
	<#if model.employee??>
		<input name="id" type="text" class="hide" style="display: none;" value="${model.employee.id}"/>
	</#if>
	<label>
		<@spring.message "employee.firstName"/>
		<#if model.binderrors?? && model.binderrors.hasFieldErrors("firstName")>
			<span class="error"><@spring.message model.binderrors.getFieldError("firstName").getCode()/></span>
		</#if>
		<input name="firstName" type="text" class="width-100"
			<#if model.employee??>
				value="${model.employee.firstName}"
				<#if model.viewOnly || (!model.viewOnly && model.employee.active?? && !model.employee.active)>
					readonly
				</#if>
			</#if>
		/>
	</label>
	
	<label>
		<@spring.message "employee.lastName"/>
		<#if model.binderrors?? && model.binderrors.hasFieldErrors("lastName")>
			<span class="error"><@spring.message model.binderrors.getFieldError("lastName").getCode()/></span>
		</#if>
		<input name="lastName" type="text" class="width-100"
			<#if model.employee??>
				value="${model.employee.lastName}"
				<#if model.viewOnly || (!model.viewOnly && model.employee.active?? && !model.employee.active)>
					readonly
				</#if>
			</#if>
		/>
	</label>
	
	<label>
		<@spring.message "employee.departmentName"/>
		<select name="departmentId" class="width-100"
			<#if model.viewOnly || (!model.viewOnly && model.employee.active?? && !model.employee.active)>
				disabled="disabled"
			</#if>
		>
			<#list model["departments"] as depart>
	    		<option value="${depart.id}"
	    			<#if model.employee?? && depart.id == model.employee.departmentId>
	    				selected="selected"
	    			</#if>
	    		>${depart.name}</option>
    		</#list> 
		</select>
		<#if model.viewOnly || (!model.viewOnly && model.employee.active?? && !model.employee.active)>
			<input name="departmentId" type="text" style="display: none;"
				<#if model.employee??>
					value="${model.employee.departmentId}"
				</#if>
			/>
		</#if>
	</label>
	
	<label>
		<@spring.message "employee.salary"/>
		<#if model.binderrors?? && model.binderrors.hasFieldErrors("salary")>
			<span class="error"><@spring.message model.binderrors.getFieldError("salary").getCode()/></span>
		</#if>
		<input name="salary" type="text" class="width-100"
			<#if model.employee??>
				value="${model.employee.salary?string["0.##"]}"
				<#if model.viewOnly || (!model.viewOnly && model.employee.active?? && !model.employee.active)>
					readonly
				</#if>
			</#if>
		/>
	</label>

	<label>
		<@spring.message "employee.birthdate"/>
		<#if model.binderrors?? && model.binderrors.hasFieldErrors("birthdate")>
			<span class="error"><@spring.message model.binderrors.getFieldError("birthdate").getCode()/></span>
		</#if>
		<input name="birthdate" type="text" placeholder="dd.mm.yyyy" class="width-100"
			<#if model.employee??>
				value="${model.employee.birthdate?string["dd.MM.yyyy"]}"
				<#if model.viewOnly || (!model.viewOnly && model.employee.active?? && !model.employee.active)>
					readonly
				</#if>
			</#if>
		/>
	</label>
	
	<label>
		<@spring.message "employee.active"/>
		<input name="active" type="checkbox" 
			<#if model.employee?? && model.employee.active>
				checked
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
		<a href="../employees.html" class="btn"><@spring.message "edit.button.cancel"/></a>
	</div>
</form>

</body>
</html>