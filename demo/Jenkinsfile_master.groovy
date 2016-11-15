#!/usr/bin/groovy

// master -> staging/production

@Library('github.com/blindpirate/fabric8-pipeline-library@master')
def utils = new io.fabric8.Utils()

def propertiesArray = [
        parameters([choice(choices: 'Normal\nRollback', description: '', name: 'BuildType')]),
        pipelineTriggers([$class: "SCMTrigger", scmpoll_spec: "* * * * *"])
];

properties(propertiesArray)


node {
    def version;
    def projectName;
    def repo
    def branch;

    if (BuildType == 'Rollback') {
        doRollback(projectName: projectName, branch: 'master');
    } else {
        version = determineVersion(branch: branch, projectName: projectName);

        git url:"${repo}",branch:branch

        stage 'Canary Release';

        sh "./gradlew bI -PimageVersion=${version}"

        stage 'Test';

        applyVersionWithLocalYamls(version: version, env: 'Test')

        stage 'Staging';

        applyVersionWithLocalYamls(version: version, env: 'Staging')

        input 'Promotion?';

        applyVersionWithLocalYamls(version: version, env: 'Production')

        stage 'Production';
    }
}