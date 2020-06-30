pipeline {
    agent {
        docker {
            label 'main'
            image docker.build("storj-ci", "--pull https://github.com/storj/ci.git").id
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

        // stage('Verification') {
        //     parallel {
        //         stage('Testsuite') {
        //             environment {
        //                 STORJ_SIM_POSTGRES = 'postgres://postgres@localhost/teststorj?sslmode=disable'
        //             }
        //             steps {
        //                 sh 'psql -U postgres -c \'create database teststorj;\''
        //                 sh './scripts/test-java.sh'
        //             }

        //             post {
        //                 always {
        //                     junit 'uplink-java/build/test-results/test/TEST-io.storj.UplinkTest.xml'
        //                 }
        //             }
        //         }
        //     }
        // }
    }

    post {
        always {
            sh "chmod -R 777 ." // ensure Jenkins agent can delete the working directory
            deleteDir()
        }
    }
}
