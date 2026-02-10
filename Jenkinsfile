pipeline {
    agent {
        docker {
            image 'maven:3.9.9-eclipse-temurin-21'
        }
    }

    parameters {
        string(
            name: 'BRANCH_NAME',
            defaultValue: 'develop',
            description: 'Nombre de la rama a construir'
        )
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Building branch: ${params.BRANCH_NAME}"
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/${params.BRANCH_NAME}"]],
                    userRemoteConfigs: [[
                        url: 'https://github.com/erickperez091/inventory-common.git'
                    ]]
                ])
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'nexus-creds',
                    usernameVariable: 'NEXUS_USER',
                    passwordVariable: 'NEXUS_PASS'
                )]) {
                    configFileProvider([configFile(
                        fileId: 'maven-settings-nexus',
                        variable: 'MAVEN_SETTINGS'
                    )]) {
                        sh '''
                          mvn deploy \
                            -s $MAVEN_SETTINGS \
                            -DskipTests
                        '''
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'commons-lib published successfully in Nexus'
        }
        failure {
            echo 'Error publishing commons-lib'
        }
    }
}
