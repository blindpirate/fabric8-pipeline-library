#!/usr/bin/groovy

@Library('github.com/blindpirate/fabric8-pipeline-library@master')
def utils = new io.fabric8.Utils()

def propertiesArray = [
        pipelineTriggers([[$class: "SCMTrigger", scmpoll_spec: "* * * * *"]])
];

properties(propertiesArray)

def version;
def projectName;
def branch;
def repo

node {
    version = determineVersion(branch: branch, projectName: projectName);

    git url:"${repo}",branch:branch

    stage 'Canary Release';

    sh "./gradlew bI -PimageVersion=${version}"

    stage 'Test';

    applyVersionWithLocalYamls(version: version, env: 'Test')

    stage 'Staging';

    applyVersionWithLocalYamls(version: version, env: 'Staging')
}
