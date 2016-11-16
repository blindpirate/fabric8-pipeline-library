#!/usr/bin/groovy

def call(Map map) {
    return call(map.version, map.env)
}

def call(String projectName, String version, String files) {

    def dockerDaemon = "dockerdaemon-public.grootapp.com:4243";
    def dockerRegistry = "registry.grootapp.com:5000";


    sh "rm -rf dockerfile.tar.gz"
    sh "tar -czf dockerfile.tar.gz ${files}"
    sh "curl -X POST -H 'Content-Type:application/json' --data-binary '@dockerfile.tar.gz' http://${dockerDaemon}/build?t=${projectName}"
    sh "curl -X POST http://${dockerDaemon}/images/${projectName}/tag?repo=${dockerRegistry}/${projectName}\\&force=1\\&tag=${version}"
    sh "curl -X POST -L --post301 -H 'X-Registry-Auth:123' http://${dockerDaemon}/images/${dockerRegistry}/${projectName}/push"
}