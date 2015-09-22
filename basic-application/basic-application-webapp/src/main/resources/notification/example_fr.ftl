<#ftl strip_text=true>
<#if SUBJECT!false>Exemple de notification - ${userFullName}</#if>

<#if BODY_TEXT!false>
Bonjour,

Quelque chose est arrivé à l'utilisateur ${userFullName} le ${date?date?string.short}.

Vous pouvez accéder à la fiche de cet utilisateur en cliquant sur le lien ci-dessous :
${url}

-- BasicApplication
</#if>