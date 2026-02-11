pipeline {
    agent {
        docker-agent {
            image 'maven:3.9.9-eclipse-temurin-21'
        }
    }

    parameters {
        string(
            name: 'BRANCH',
            defaultValue: 'feature/update-pipeline',
            description: 'Branch to build'
        )
    }

    environment {
        GIT_REPO = 'https://github.com/erickperez091/inventory-common.git'
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Building branch: ${params.BRANCH}"
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/${params.BRANCH}"]],
                    userRemoteConfigs: [[url: env.GIT_REPO]]
                ])
            }
        }

        stage('Read version from POM') {
            steps {
                script {
                    def pom = readMavenPom file: 'pom.xml'
                    env.PROJECT_VERSION = pom.version
                    echo "Version detected: ${env.PROJECT_VERSION}"
                }
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

    post {
        success {
            echo "SUCCESS: ${params.BRANCH} → ${env.PROJECT_VERSION}"
        }
        failure {
            echo "FAILED: ${params.BRANCH}"
        }
    }
}
