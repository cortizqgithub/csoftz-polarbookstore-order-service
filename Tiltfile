# Build
custom_build(
    # Name of the container image
    ref = 'order-service',
    tag = 'latest'
    # Command to build the container image
    command = 'mvnw clean -DskipTests=true -Ddocker.image.name=order-service:latest spring-boot:build-image',
    # Files to watch that trigger a new build
    deps = ['pom.xml', 'src']
)

# Deploy
k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml'])

# Manage
k8s_resource('order-service', port_forwards='9003:9002'))