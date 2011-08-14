<#assign snippit_types = types>
<#assign snippit_to_edit = snippit>
<html>
<body>
<form action="/api/snippit/update/${snippit_to_edit.id}" method="post">
Snippit Name: <input type="text" name="name" value="${snippit_to_edit.name}" /> <br />
Snippit Text: <input type="text" name="snippit" value="${snippit_to_edit.snippit}" /> <br />
<#list snippit_types as type>
<input type="radio" name="type" value="${type}" <#if type == snippit.type> checked </#if>>${type}<br />
</#list>
<input type="submit" value="submit" /> <br />
</form>
</body>
</html>