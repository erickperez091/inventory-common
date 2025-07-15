pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'Maven 3.9.6'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/erickperez091/inventory-common.git' // reemplázalo con tu repo real
            }
        }

        stage('Build') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean package -DskipTests"
            }
        }

        stage('Upload to Nexus') {
            steps {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: 'nexus:8081',
                    groupId: 'com.example',
                    version: '1.0.1',
                    repository: 'maven-test-releases',
                    credentialsId: 'nexus-creds',
                    artifacts: [
                        [artifactId: 'commons-lib', classifier: '', file: 'target/commons-lib-1.0.1.jar', type: 'jar']
                    ]
                )
            }
        }
    }
}