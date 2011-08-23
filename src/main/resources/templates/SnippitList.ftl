SnippitList      <br />
<br />
<br />

<#assign snippit_list = set>
<#list snippit_list as snippit>
  ${snippit.name} <a href="/updateSnippit?id=${snippit.id}">Edit Snippit</a> <a href="/api/snippit/delete/${snippit.id}">Delete Snippit</a><br />
</#list>