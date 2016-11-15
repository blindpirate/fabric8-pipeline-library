#!/usr/bin/groovy

def call(Map map) {
    return call(map.projectName, map.branch, map.env);
}

def call(String projectName, String branch, String env) {

    def dockerRegistryHost = "http://registry.grootapp.com:5000";
    def getAllTagsUri = "/v2/${projectName}/tags/list";

    def responseJson = new URL("${dockerRegistryHost}${getAllTagsUri}")
            .getText(requestProperties: ['Content-Type': "application/json"]);

    println(responseJson)

    // {name:xxx,tags:[tag1,tag2,...]}
    Map response = new groovy.json.JsonSlurperClassic().parseText(responseJson) as Map;

    def versionsStr = response.tags.join('\n');

    def rollbackVersion = input(
            id: 'versonSelect',
            message: 'Select a version to rollback',
            ok: 'OK',
            parameters: [choice(choices: versionsStr)])

    applyVersionWithLocalYamls(version: rollbackVersion, env: env);

}


