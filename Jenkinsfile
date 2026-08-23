pipeline {
    agent any

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 20, unit: 'MINUTES')
    }

    environment {
        // Docker Hub images must be named <username>/<image>
        DOCKERHUB_USER = "prity987"
        IMAGE_NAME     = "${DOCKERHUB_USER}/jenkins-demo-app"
        IMAGE_TAG      = "${env.BUILD_NUMBER}"
        MAVEN_OPTS     = "-Xmx384m"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn -B package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                    docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                    docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
                """
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DH_USER',
                        passwordVariable: 'DH_TOKEN')]) {
                    sh '''
                        echo "$DH_TOKEN" | docker login -u "$DH_USER" --password-stdin
                        docker push "$IMAGE_NAME:$IMAGE_TAG"
                        docker push "$IMAGE_NAME:latest"
                        docker logout
                    '''
                }
            }
        }

        stage('Trigger CD') {
            steps {
                // Automatically start the CD job, deploying EXACTLY the
                // image this build just pushed (not 'latest').
                // wait: false -> CI finishes green without waiting for CD.
                build job: 'demo-app-CD',
                      parameters: [string(name: 'IMAGE_TAG', value: "${env.BUILD_NUMBER}")],
                      wait: false
            }
        }
    }

    post {
        success {
            echo "CI SUCCESS - pushed ${IMAGE_NAME}:${IMAGE_TAG} to Docker Hub and triggered CD."
        }
        failure {
            echo "CI FAILED - check the stage logs above."
        }
    }
}
