<#import "/spring.ftl" as spring />
<@spring.bind "model" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title><@spring.message "titles.departments"/></title>
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

<a class="btn btn-small btn-blue" href="./edit/department.html?action=new"><@spring.message "table.add"/></a>
<div class="group right">
	<a class="btn btn-outline btn-small" style="margin-right: 5px;" href="./employees.html"><@spring.message "titles.employees"/></a>
	<a class="btn btn-yellow btn-outline btn-small" href="./../j_spring_security_logout"><@spring.message "button.logout"/></a>
</div>

<table class="table-bordered table-hovered width-100">
	<col>
	<col width="120px">
	<col width="120px">
	<col width="120px">
	<thead>
		<tr>
			<th><@spring.message "department.name"/></th>
			<th colspan="3"></th>
    	</tr>
	</thead>
    <tbody>
    	<form class="forms">
    	    <tr>
        		<td><input name="name" class="width-100" type="text" placeholder="<@spring.message "department.name"/>" 
        			<#if model.name??>value="${model.name}"</#if>></td>
				<td colspan="2"><input type="submit" class="btn btn-small width-100" value="<@spring.message "table.search"/>"></td>
				<td><a href="/pages/departments.html" class="btn btn-small width-100"><@spring.message "table.search-clear"/></td>
			</tr>
		</form>
    	<#list model["departments"] as depart>
    		<tr>
        		<td>${depart.name}</td>
        		<td><a class="btn btn-smaller btn-blue width-100" href="./view/department.html?id=${depart.id}"><@spring.message "table.view"/></a></td>
        		<td>
        			<#if !model.viewOnly>
        				<a class="btn btn-smaller btn-green width-100" href="./edit/department.html?id=${depart.id}"><@spring.message "table.edit"/></a>
        			</#if>
        		</td>	
				<td>
					<#if !model.viewOnly>
						<a class="btn btn-smaller btn-red width-100" href="./delete/department.html?id=${depart.id}"><@spring.message "table.delete"/></a>
					</#if>
				</td>
			</tr>
    	</#list> 
	</tbody>
</table>

</body>
</html>