<#import "/spring.ftl" as spring />
<@spring.bind "model" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title><@spring.message "titles.employees"/></title>
	<link rel="stylesheet" href="./../css/kube.css">
	
	<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.js"></script>
	<script src="./../js/kube.js"></script>
</head>
<body>

<#if spring.status.error>
	<div class="width-100">
		<#list spring.status.errors.globalErrors as error>
			<span class="label label-red width-100">${error.defaultMessage}</li>   
		</#list>
	</div>
</#if>

<a class="btn btn-small btn-blue" href="./edit/employee.html?action=new"><@spring.message "table.add"/></a>
<div class="group right">
	<a class="btn btn-outline btn-small" style="margin-right: 5px;" href="./departments.html"><@spring.message "titles.departments"/></a>
	<a class="btn btn-yellow btn-outline btn-small" href="./../j_spring_security_logout"><@spring.message "button.logout"/></a>
</div>

<table class="table-bordered table-hovered width-100">
	<col>
	<col>
	<col>
	<col>
	<col>
	<col>
	<col width="120px">
	<col width="120px">
	<col width="120px">
	<thead>
		<tr>
			<th><@spring.message "employee.firstName"/></th>
			<th><@spring.message "employee.lastName"/></th>
			<th><@spring.message "employee.departmentName"/></th>
			<th><@spring.message "employee.salary"/></th>
			<th><@spring.message "employee.birthdate"/></th>
			<th><@spring.message "employee.active"/></th>
			<th colspan="3"></th>
    	</tr>
	</thead>
    <tbody>
    	<form class="forms">
    	    <tr>
        		<td><input name="firstName" class="width-100" type="text" placeholder="<@spring.message "employee.firstName"/>" 
        			<#if model.firstName??>value="${model.firstName}"</#if>></td>
        		<td><input name="lastName" class="width-100" type="text" placeholder="<@spring.message "employee.lastName"/>"
        			<#if model.lastName??>value="${model.lastName}"</#if>></td>
				<td><input name="departmentName" class="width-100" type="text" placeholder="<@spring.message "employee.departmentName"/>"
					<#if model.departmentName??>value="${model.departmentName}"</#if>></td>
        		<td><!-- <input name="salary" class="width-100" type="text" placeholder="<@spring.message "employee.salary"/>"
        			<#if model.salary??>value="${model.salary?string["0.##"]}"</#if>> --></td>	
        		<td><input name="birthdate" class="width-100" type="text" placeholder="dd.mm.yyyy"
        			<#if model.birthdate??>value="${model.birthdate?string["dd.MM.yyyy"]}"</#if>></td>
        		<td><input name="active" class="width-100" type="checkbox" placeholder="<@spring.message "employee.active"/>" 
        			<#if model.active??>checked value="${model.active?c}"</#if>></td>
				<td colspan="2"><input type="submit" class="btn btn-small width-100" value="<@spring.message "table.search"/>"></td>
				<td><a href="/pages/employees.html" class="btn btn-small width-100"><@spring.message "table.search-clear"/></td>
			</tr>
		</form>
    	<#list model["employees"] as empl>
    		<tr>
        		<td>${empl.firstName}</td>
        		<td>${empl.lastName}</td>
				<td>${empl.departmentName}</td>
        		<td>${empl.salary}</td>	
        		<td>${empl.birthdate?string["dd.MM.yyyy"]}</td>
        		<td><input type="checkbox" disabled="disabled" <#if empl.active>checked value="true"</#if>></td>	
        		<td><a class="btn btn-smaller btn-blue width-100" href="./view/employee.html?id=${empl.id}"><@spring.message "table.view"/></a></td>
        		<td>
        			<#if !model.viewOnly>
        				<a class="btn btn-smaller btn-green width-100" href="./edit/employee.html?id=${empl.id}"><@spring.message "table.edit"/></a>
        			</#if>
        		</td>	
				<td>
					<#if !model.viewOnly>
						<a class="btn btn-smaller btn-red width-100" href="./delete/employee.html?id=${empl.id}"><@spring.message "table.delete"/></a>
					</#if>	
				</td>
			</tr>
    	</#list> 
	</tbody>
</table>

</body>
</html>