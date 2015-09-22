<#ftl strip_text=true>
<#if SUBJECT!false>Example notification - ${userFullName}</#if>

<#if BODY_TEXT!false>
Hello,

Something happened to user ${userFullName} on ${date?date?string.short}.

You may access the user description by clicking on the link below:
${url}

-- BasicApplication
</#if>