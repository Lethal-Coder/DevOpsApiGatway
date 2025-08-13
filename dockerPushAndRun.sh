cd /Users/lethal/Desktop/GPT_Java_project/Demo_Project_Devops/my-spring-app-client
mvn clean package -DskipTests
# Build the Docker image
docker build -t devopsforce/my-spring-app-client:latest .

# Push the image to Docker Hub
docker push devopsforce/my-spring-app-client:latest

# Pull the image from Docker Hub (optional, for testing on another machine)
docker pull devopsforce/my-spring-app-client:latest
cd /Users/lethal/Desktop/GPT_Java_project/Demo_Project_Devops
# Run the Docker container
docker run -p 8081:8081 devopsforce/my-spring-app-client:latest