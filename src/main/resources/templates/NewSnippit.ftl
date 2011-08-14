<#assign snippit_types = types>
<html>
<body>
<form action="/api/snippit/new" method="post">
Snippit Name: <input type="text" name="name" /> <br />
Snippit Text: <input type="text" name="snippit" /> <br />
<#list snippit_types as type>
<input type="radio" name="type" value="${type}">${type}<br />
</#list>
<input type="submit" value="submit" /> <br />
</form>
</body>
</html>