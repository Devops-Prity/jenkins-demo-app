# jenkins-demo-app
Spring Boot demo application for the Jenkins CI/CD practical.

- `Jenkinsfile`    -> CI pipeline (build, test, package, push to Docker Hub, trigger CD)
- `Jenkinsfile.cd` -> CD pipeline (pull from Docker Hub, deploy container, health check)
- App runs on port **8081** (`/` and `/health` endpoints)
