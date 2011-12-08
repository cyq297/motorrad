<html>
<body>
<form action="/api/role/new" method="post">
Snippit Name: <input type="text" name="name" /> <br />
<#assign listitems = snippits.listElements.values()>
<input type="submit" value="submit" /> <br />
</form>
</body>
</html>