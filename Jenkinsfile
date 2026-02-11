pipeline {
    agent { label 'docker-agent' }

    parameters {
        string(name: 'BRANCH', defaultValue: 'develop', description: 'Git branch')
    }

    environment {
        GIT_REPO = 'https://github.com/erickperez091/inventory-common.git'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/${params.BRANCH}"]],
                    userRemoteConfigs: [[url: env.GIT_REPO]]
                ])
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -v'
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
                        sh 'mvn deploy -s $MAVEN_SETTINGS -DskipTests'
                    }
                }
            }
        }
    }
}
