pipeline {
    agent any

    stages {
        stage('Parallel') {
            parallel {
                stage('Parallel-1') {
                    agent any                    
                    steps {
                        sh 'echo Parallel-1'
                    }
                }
                stage('Parallel-2') {
                    agent any                    
                    steps {
                        sh 'echo Parallel-2'
                    }
                }
            }
        }
        stage('Test') {
            parallel {
                stage('Test-Normal') {
                    agent any                    
                    steps {
                        sh 'echo Test-Normal'
                    }
                }
                stage('Test-Skip') {
                    when { expression { 'SKIP' == 'TRUE' } }
                    agent any                    
                    steps {
                        sh 'echo Test-Skip'
                    }
                }
                stage('Test-Catch-Failure') {
                    agent any                    
                    steps {
                        script {
                            try {
                                sh 'idontexist'
                            } catch (Exception err) {
                                currentBuild.result = 'UNSTABLE'
                            }
                        }
                    }
                }
            }
        }
        stage('Archive') {
            steps {
                deleteDir()
                sh 'echo "This is my testing artifact." >> artifact.txt'
            }
            post {
                always {
                    archiveArtifacts "*.txt"
                }
            }
        }
    }
}
