pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'Maven 3.9.6'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/erickperez091/inventory-common.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean package"
            }
        }

        stage('Upload to Nexus') {
            steps {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: 'nexus:8081',
                    groupId: 'com.example',
                    version: '1.0.0',
                    repository: 'maven-releases',
                    credentialsId: 'nexus-creds',
                    artifacts: [
                        [artifactId: 'commons-lib', classifier: '', file: 'target/commons-lib-1.0.1.jar', type: 'jar']
                    ]
                )
            }
        }
    }
}