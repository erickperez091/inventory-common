pipeline {
    agent { label 'docker-agent' }

    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'develop', description: 'Nombre de la rama a construir')
        string(name: 'VERSION', defaultValue: '1.0.1', description: 'Versión del artefacto')
    }

    environment {
        MAVEN_HOME = tool 'Maven 3.9.6'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Building branch: ${params.BRANCH_NAME}"
                checkout([$class: 'GitSCM',
                          branches: [[name: "*/${params.BRANCH_NAME}"]],
                          userRemoteConfigs: [[url: 'https://github.com/erickperez091/inventory-common.git']]])
            }
        }
        
        stage('Test')    {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean test"
            }
        }

        stage('Build') {
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
                    version: "${params.VERSION}",
                    repository: 'maven-test-releases',
                    credentialsId: 'nexus-creds',
                    artifacts: [
                        [artifactId: 'commons-lib', classifier: '', file: "target/commons-lib-${params.VERSION}.jar", type: 'jar'],
                        [artifactId: 'commons-lib', classifier: '', file: 'pom.xml', type: 'pom']
                    ]
                )
            }
        }
    }
    post {
        success { echo 'commons-lib published successfully in Nexus' }
        failure { echo 'Error publishing commons-lib' }
    }
}
