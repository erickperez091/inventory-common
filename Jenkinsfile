pipeline {
    agent {
        docker {
            image 'maven:3.9.9-eclipse-temurin-21'
        }
    }

    parameters {
        gitParameter(
            name: 'BRANCH',
            type: 'PT_BRANCH',
            defaultValue: 'develop',
            branchFilter: 'origin/(.*)',
            sortMode: 'DESCENDING_SMART',
            description: 'Select Git branch to build'
        )
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Building branch: ${params.BRANCH}"
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/${params.BRANCH}"]],
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
                withCredentials([
                    usernamePassword(
                        credentialsId: 'nexus-creds',
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASS'
                    )
                ]) {
                    configFileProvider([
                        configFile(
                            fileId: 'maven-settings-nexus',
                            variable: 'MAVEN_SETTINGS'
                        )
                    ]) {
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
}
