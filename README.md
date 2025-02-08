# TaskManagerTutorial
TaskManager Webservice using SpringBoot and MySQL

# Prerequisites:
Java - Open JDK 23

# Database
By Default, uses H2 database to store the data. Configuration for Mysql
is defined in pom.xml and application.properties. You can comment H2 and uncomment and update
the properties.

# How to Run the application
Compile and execute Unit Tests: mvn clean verify
Start the application: mvn spring-boot:run

# How to run in Docker container
From the root directly, run the below commands
mvn clean package - This creates a jar file in the target folder
docker build -t taskmanager . - This creates a docker image to run
docker run -d -p 8080:8080 taskmanager - This runs the docker image

Note: The application will be available at http://localhost:8080

# API Endpoints Supported:

## Create a task
POST /tasks
`{
    "summary":"Task1"
}`

### Get list of tasks
GET /tasks with filters status and priority

### Get specific task with an id
GET /tasks/{id}

### Update task information
PATCH /tasks/{id}
