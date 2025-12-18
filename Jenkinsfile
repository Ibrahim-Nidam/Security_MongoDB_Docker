pipeline {
    agent any

    tools {
        jdk 'JDK21'           // name you will configure in Jenkins Global Tools
        maven 'Maven3'        // name you will configure in Jenkins Global Tools
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn -B clean verify'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn -B package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t sdg-app:latest .'
            }
        }

        stage('Cleanup Old Containers') {
            steps {
                sh '''
                    # First try normal docker compose down
                    docker compose down --remove-orphans || true

                    # Force-remove any containers that might still exist with our fixed names
                    docker rm -f sdg-app sdg-mongo sdg-jenkins sdg-n8n || true

                    # Optional: Clean up dangling images from previous builds (keeps things tidy)
                    docker image prune -f || true
                '''
            }
        }

        stage('Start with Docker Compose') {
            steps {
                sh 'docker compose up -d'
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/**/*.xml'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
        success {
            echo 'SDG PROJECT SUCCESSFULLY BUILT AND DEPLOYED!'
        }
    }
}