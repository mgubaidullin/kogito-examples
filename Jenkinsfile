@Library('jenkins-pipeline-shared-libraries')_

pipeline {
    agent {
        label 'kogito-static || kie-rhel7'
    }
    tools {
        maven 'kie-maven-3.5.4'
        jdk 'kie-jdk1.8'
    }
    options {
        buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '10')
        timeout(time: 90, unit: 'MINUTES')
    }
    stages {
        stage('Initialize') {
            steps {
                sh 'printenv'
            }
        }
        stage('Build kogito-bom') {
            steps {
                dir("kogito-bom") {
                    script {
                        githubscm.checkoutIfExists('kogito-bom', "$CHANGE_AUTHOR", "$CHANGE_BRANCH", 'kiegroup', "$CHANGE_TARGET")
                        maven.runMavenWithSubmarineSettings('clean install', true)
                    }
                }
            }
        }
        stage('Build kogito-runtimes') {
            steps {
                dir("kogito-runtimes") {
                    script {
                        githubscm.checkoutIfExists('kogito-runtimes', "$CHANGE_AUTHOR", "$CHANGE_BRANCH", 'kiegroup', "$CHANGE_TARGET")
                        maven.runMavenWithSubmarineSettings('clean install', true)
                    }
                }
            }
        }

        stage('Build kogito-cloud') {
            steps {
                dir("kogito-cloud") {
                    script {
                        githubscm.checkoutIfExists('kogito-cloud', "$CHANGE_AUTHOR", "$CHANGE_BRANCH", 'kiegroup', "$CHANGE_TARGET")
                        maven.runMavenWithSubmarineSettings('clean install', true)
                    }
                }
            }
        }
        stage('Build kogito-examples') {
            steps {
                script {
                    maven.runMavenWithSubmarineSettings('clean install', false)
                    // Don't run with tests so far, see: https://github.com/quarkusio/quarkus/issues/6885
                    maven.runMavenWithSubmarineSettings('clean install -Ppersistence', true)
                }
            }
        }
    }
    post {
        unstable {
            script {
                mailer.sendEmailFailure()
            }
        }
        failure {
            script {
                mailer.sendEmailFailure()
            }
        }
        always {
            // Currently there are no tests in submarine-examples
            //junit '**/target/surefire-reports/**/*.xml'
            cleanWs()
        }
    }
}
