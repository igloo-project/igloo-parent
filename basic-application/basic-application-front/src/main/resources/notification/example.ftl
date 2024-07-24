<#ftl strip_text=true>
<#if SUBJECT!false>Example notification · ${userFullName} · ${date?date?string.short}, at ${date?time?string.short}</#if>

<#if BODY_TEXT!false>
User ${userFullName} did something on ${date?date?string.short}, at ${date?time?string.short}.

To access the application, click on the link below:
${url}

-- BasicApplication
</#if>