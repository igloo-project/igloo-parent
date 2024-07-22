# Igloo project Init

Igloo project init pipeline allows to automate the initialization of BasicApplication in a repository by GitLab Api.

## Use

You have to call like this to create pipeline with appropriate permissions for target repository (GIT_HTTPS_REPO_PROJECT and PRIVATE-TOKEN) :

```
curl --location --request POST 'https://gitlab.tools.kobalt.fr/api/v4/projects/igloo-project%2Figloo-parent/pipeline' \
--header 'PRIVATE-TOKEN: XXX' \
--header 'Content-Type: application/json' \
--data-raw '{
    "ref":"igloo-boot-dev",
    "variables":[
        {"key":"ARTIFACT_ID", "variable_type" : "env_var", "value" : "hello-basicapp"},
        {"key":"GROUP_ID", "variable_type" : "env_var", "value" : "fr.hello.basicapp"},
        {"key":"VERSION", "variable_type" : "env_var", "value" : "0.1-SNAPSHOT"},
        {"key":"PACKAGE", "variable_type" : "env_var", "value" : "fr.hello.basicapp"},
        {"key":"ARCHETYPE_APPLICATION_NAME_PREFIX", "variable_type" : "env_var", "value" : "HelloBasicapp"},
        {"key":"ARCHETYPE_SPRING_ANNOTATION_VALUE_PREFIX", "variable_type" : "env_var", "value" : "HelloBasicapp"},
        {"key":"ARCHETYPE_FULL_APPLICTION_NAME", "variable_type" : "env_var", "value" : "Customer - Hello Basicapp"},
        {"key":"ARCHETYPE_DATABASE_PREFIX", "variable_type" : "env_var", "value" : "hello_basicapp"},
        {"key":"GIT_HTTPS_REPO_PROJECT", "variable_type" : "env_var", "value" : "https://gitlab.tools.kobalt.fr/kobalt/hello-basicapp.git"},
        {"key":"TARGET_BRANCH", "variable_type" : "env_var", "value" : "alternative-branch"},
        {"key":"GIT_ACCESS_TOKEN", "variable_type" : "env_var", "value" : "XXX"},
        {"key":"DRY_RUN", "variable_type" : "env_var", "value" : "true"}
    ]
}'
```
