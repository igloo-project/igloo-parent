<#ftl strip_text=true>
<#if SUBJECT!false>Exemple de notification · ${userFullName} · ${date?date?string.short} à ${date?time?string.short}</#if>

<#if BODY_TEXT!false>
L'utilisateur ${userFullName} a effectué une opération le ${date?date?string.short} à ${date?time?string.short}.

Pour accéder à l'application, cliquez sur le lien ci-dessous :
${url}

-- BasicApplication
</#if>