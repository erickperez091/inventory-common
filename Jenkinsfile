pipeline {
    agent {
        docker {
            image 'maven:3.9.9-eclipse-temurin-21'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Building branch: ${env.BRANCH_NAME}"
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('Deploy to Nexus') {
            when {
                anyOf {
                    branch 'develop'
                    branch 'main'
                    branch 'release/*'
                }
            }
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
            echo 'commons-lib published successfully in Nexus'
        }
        failure {
            echo 'Error publishing commons-lib'
        }
    }
}
