#!/usr/bin/groovy
import groovy.json.JsonSlurper


def call(Map map) {
    return call(map.projectName, map.branch, map.env);
}

def call(String projectName, String branch, env) {

    def dockerRegistryHost = "http://registry.grootapp.com:5000";
    def getAllTagsUri = "/v1/repositories/${projectName}/tags";

    def responseJson = new URL("${dockerRegistryHost}${getAllTagsUri}")
            .getText(requestProperties: ['Content-Type': "application/json"]);

    println(responseJson)

    // {tag1:digest1,tag2:digest2,...}
    Map response = new JsonSlurper().parseText(responseJson) as Map;

    def versions = response.keySet();

    def versionsStr = versions.join('\n');

    def rollbackVersion = input(message: 'Select a version to rollback', ok: 'OK', [choice(choices: versionsStr)])

    applyVersionWithLocalYamls(version: rollbackVersion, env: 'Production');

}


