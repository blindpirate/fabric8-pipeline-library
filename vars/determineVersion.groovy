#!/usr/bin/groovy

def call(Map map) {
    return call(map.projectName, map.branch)
}

def call(String projectName, String branch) {
    String timestamp = new Date().format('yyyyMMddHHmmss')
    return "${projectName}-${branch}-${timestamp}";
}
