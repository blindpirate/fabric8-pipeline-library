#!/usr/bin/groovy

def call(String projectName, String branch = 'master') {
    String timestamp = new Date().format('yyyyMMddHHmmss')
    return "${projectName}-${branch}-${timestamp}";
}
