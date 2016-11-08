#!/usr/bin/groovy
def call(body) {

    def label = "buildpod.${env.JOB_NAME}.${env.BUILD_NUMBER}".replace('-', '_').replace('/', '_')
      podTemplate(label: label, serviceAccount: 'jenkins', containers: [
            [name: 'client', image: 'registry.grootapp.com:5000/groot-builder', command: 'cat', ttyEnabled: true, envVars: [
                    [key: 'DOCKER_CONFIG', value: '/home/jenkins/.docker/'],
                    [key: 'KUBERNETES_MASTER', value: 'kubernetes.default']]],
            [name: 'jnlp', image: 'iocanel/jenkins-jnlp-client:latest', command:'/usr/local/bin/start.sh', args: '${computer.jnlpmac} ${computer.name}', ttyEnabled: false,
                    envVars: [[key: 'DOCKER_HOST', value: 'unix:/var/run/docker.sock']]]],
            volumes: [
                    [$class: 'SecretVolume', mountPath: '/home/jenkins/.docker', secretName: 'jenkins-docker-cfg'],
                    [$class: 'HostPathVolume', mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'],
                    [$class: 'SecretVolume', mountPath: '/home/jenkins/.ssh', secretName: 'jenkins-ssh-config'],
                    [$class: 'SecretVolume', mountPath: '/home/jenkins/.ssh-git', secretName: 'jenkins-git-ssh']
            ]) {
        node(label) {
            body()
        }
    }
}
