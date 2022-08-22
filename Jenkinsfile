pipeline {
    agent {
        docker {
            label 'main'
            image docker.build("storj-ci", "--pull https://github.com/storj/ci.git#main:android").id
            args '-u root:root --cap-add SYS_PTRACE -v "/tmp/gomod":/go/pkg/mod'
        }
    }
    options {
          timeout(time: 26, unit: 'MINUTES')
    }
    stages {
        stage('Build') {
            steps {
                checkout scm

                sh 'mkdir -p .build'

                sh 'service postgresql start'
            }
        }

        stage('Verification') {
            parallel {
                stage('Testsuite') {
                    environment {
                        ANDROID_HOME = '/opt/android-sdk-linux'
                        STORJ_SIM_POSTGRES = 'postgres://postgres@localhost/teststorj?sslmode=disable'
                    }
                    steps {
                        sh 'psql -U postgres -c \'create database teststorj;\''
                        sh './scripts/test-android.sh'
                    }

                    post {
                        always {
                            junit 'uplink-android/build/outputs/androidTest-results/connected/TEST-android_libuplink_test(AVD) - 7.0-uplink-android-.xml'
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            sh "chmod -R 777 ." // ensure Jenkins agent can delete the working directory
            deleteDir()
        }
    }
}
