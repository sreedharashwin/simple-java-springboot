pipeline {
    agent any
    environment {
        PATH = "/opt/homebrew/opt/maven/bin:$PATH:/Applications/Docker.app/Contents/Resources/bin"
        kubeconfig=credentials('kube_config')
        container_registry=credentials('CONTAINER_REGISTRY')
        container_registry_user=credentials('REGISTRY_UN')
        container_registry_pw=credentials('REGISTRY_PW')
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/sreedharashwin/simple-java-springboot.git'
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        
        stage('Test') {
            steps {
                script {
                    sh 'mvn test'
                    sh 'mvn surefire-report:report-only'
                }
            }
            
            post {
                always {
                    script {
                        def reportPath = 'target/site/surefire-report.html'
                        if (fileExists(reportPath)) {
                            def reportContent = readFile(file: reportPath)
                            emailext (
                                subject: "JUnit Tests Report",
                                body: reportContent,
                                to: 'qwertyorg2@gmail.com'
                            )
                        } else {
                            echo "Report file does not exist: ${reportPath}"
                        }
                    }
                }
            }
        }

        stage('Code Quality Analysis') {
            steps {
                script {
                    sh "mvn clean verify sonar:sonar -Dsonar.projectKey=springbootapp -Dsonar.projectName='springbootapp' -Dsonar.token=sqp_49777e9df286eba7f587277b7b7ebd6200d71911"
                }
            }
        }
         stage('Staging'){
            steps{
                script{
                    sh 'echo Building Container for staging'
                    sh 'docker build --platform linux/amd64 -t springapp:latest .'
                    sh 'docker run -p 8080:8080 -d springapp:latest'
                }
            }
            post {
                success {
                    script {
                        emailext(
                            subject: "[Jenkins] Production Deployment Approval",
                            to: "qwertyorg2@gmail.com",
                            body: '''<h2>Click to approve</h2>
                            <p><strong>This link is valid for ten minutes. After this period, the pipeline will abort.</strong></p>
                            <p><a href="${BUILD_URL}input">Approve Deployment</a></p>'''
                        )
                    }
                }
            }
        }

        stage('User Confirmation') {
        steps {
            script {
                def proceedWithBuild = true
                try {
                    timeout(time: 10, unit: 'MINUTES') {
                        def userInput = input(
                            id: 'userInput',
                            message: 'Do you want to proceed with deployment to Production?',
                            parameters: [
                                choice(
                                    choices: ['Yes', 'Abort'],
                                    description: 'Click "Yes" to proceed or "Abort" to cancel.',
                                    name: 'PROCEED'
                                )
                            ]
                        )
                        if (userInput == 'Abort') {
                            proceedWithBuild = false
                        }
                    }
                } catch (Exception e) {
                    echo "Timeout occurred. Terminating pipeline"
                    return
                }

                if (!proceedWithBuild) {
                    currentBuild.result = 'ABORTED'
                    return
                }
            }
          }
     }
      
      stage('Production') {
            steps {
                script{
                    sh 'echo "Proceeding with pre-deployment steps"'
                    sh 'echo "Logging into container registry with docker"'

                    def loginStatus = sh(
                        script: "docker login ${container_registry} --username ${container_registry_user} --password ${container_registry_pw}",
                        returnStatus: true
                    )
                    if (loginStatus == 0) {
                         sh """
                        echo 'Docker login succeeded'
                        echo 'Proceeding to push the image'
                        docker tag springapp:latest ${container_registry}/springbootapp:${currentBuild.number}
                        docker push ${container_registry}/springbootapp:${currentBuild.number}
                        echo "Image pushed. Proceeding to create deployment and service on Azure"
                         """
                    } else {
                        error('Docker login failed')
                        sh 'echo "Check access. Aborting..."'
                        return
                    }
                        def kubeConfigPath = "${env.WORKSPACE}/KUBE_CONFIG.txt"
                        sh """
                        touch ${kubeConfigPath}
                        echo "${kubeconfig}" | base64 -d > ${kubeConfigPath}
                        curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/darwin/amd64/kubectl"
                        chmod u+x ./kubectl
                        
                        ./kubectl config use-context sit753k8s
                        ./kubectl config current-context
                        ./kubectl delete deployment my-website --ignore-not-found=true
                        ./kubectl delete service my-website-service --ignore-not-found=true
                        sleep 10
                        
                        ./kubectl create deployment my-website --image=${container_registry}/springbootapp:${currentBuild.number} --port=8080
                        ./kubectl expose deployment my-website --type=LoadBalancer --name=my-website-service --port=8080 --target-port=8080

                        rm ${kubeConfigPath}
                    """
                }
            }
	    }
    }
}
