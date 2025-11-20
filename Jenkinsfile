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

        stage('Stop & Remove old container (if exists)') {
            steps {
                sh 'docker compose down || true'
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