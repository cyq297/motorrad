SnippitList      <br />
<br />
<br />

<#assign snippit_list = set>
<#list snippit_list as snippit>
  ${snippit.name} ${snippit.type} <br />
</#list>