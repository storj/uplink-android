pipeline {
    agent {
        label 'main'
    }
    options {
          timeout(time: 26, unit: 'MINUTES')
    }
    stages {
        stage('Build CI image') {
            steps {
                script {
                    docker.build("storj-ci", "--pull https://github.com/storj/ci.git#main:android")
                }
            }
        }
        stage('Verification') {
            parallel {
                stage('Testsuite') {
                    agent {
                        docker {
                            label 'main'
                            // Run everything on the same node so we only have to build the docker image once.
                            // Alternatively we can push the image to a registry.
                            reuseNode true
                            image 'storj-ci'
                            args '-u root:root --cap-add SYS_PTRACE -v "/tmp/gomod":/go/pkg/mod'
                        }
                    }
                    environment {
                        ANDROID_HOME = '/opt/android-sdk-linux'
                        STORJ_SIM_POSTGRES = 'postgres://postgres@localhost/teststorj?sslmode=disable'
                    }
                    steps {
                        sh 'mkdir -p .build'
                        sh 'service postgresql start'
                        sh 'psql -U postgres -c \'create database teststorj;\''
                        sh './scripts/test-android.sh'
                    }

                    post {
                        always {
                            sh "chmod -R 777 ." // ensure Jenkins agent can delete the working directory
                            junit 'uplink-android/build/outputs/androidTest-results/connected/TEST-android_libuplink_test(AVD) - 7.0-uplink-android-.xml'
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            node(null) {
                cleanWs()
                deleteDir()
            }
        }
    }
}
