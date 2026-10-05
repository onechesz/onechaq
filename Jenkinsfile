pipeline {
    agent any
    stages {
        stage('Собрать приложение') {
            steps {
                sh '''
                    set -e
                    cd /workspace
                    sed -i 's/\\r$//' mvnw
                    bash ./mvnw package -DskipTests
                '''
            }
        }
        stage('Собрать образ') {
            steps {
                sh '''
                    set -e
                    cd /workspace
                    docker build -f src/main/docker/Dockerfile.jvm -t onechaq:1.0 .
                '''
            }
        }
    }
}
