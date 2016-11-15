#!/usr/bin/groovy

def call(String version, String env) {
    def dockerRegistryHost = "registry.grootapp.com:5000";
    String deployment = readFile("deploy/${env}/deployment.yaml");

    deployment = deployment.replaceAll(/\$\{version\}/, version);

    def namespace = extractNamespaceFromYaml(deployment);

    kubernetesApply(file: deployment, environment: namespace);
}

def extractNamespaceFromYaml(String yaml) {
    def matcher = yaml =~ /\s+namespace:\s*(\w+)/
    return matcher[0][1]
}
