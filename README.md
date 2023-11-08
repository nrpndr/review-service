# review-service
Review Service is a service that takes care of maintaining reviews and star rating for a movies and events

## Getting Started
It a spring boot project with Maven.
Clone the github Repo and import in IDE of your choice. \
**Notes:**
- If you have the zipped project, just unzip it and import in any IDE

## Built With
* [Java 17](https://openjdk.org/projects/jdk/17/)
* [Maven](https://maven.apache.org/)
* [Spring boot 3.1.5](https://spring.io/projects/spring-boot)
* [Spring security](https://spring.io/projects/spring-security)
* [Swagger](https://swagger.io/)
* [Docker](https://docs.docker.com/)
* [Mongodb](https://www.mongodb.com/)

### Prerequisites
- Java 17
- Maven 3.6.3
- Docker Desktop 24.X.X (If you plan to deploy and test in local)

### Installing
- Install java 17 \
  You can use [opendjdk 17](https://download.java.net/openjdk/jdk17/ri/openjdk-17+35_windows-x64_bin.zip) and configure your PATH to use this version
- Install Maven \
  Visit [this link](https://maven.apache.org/install.html) in order to install maven in your local.
- Install Docker Desktop \
  Visit [this link](https://docs.docker.com/desktop/install/mac-install/). If you are a windows desktop user refer [this](https://docs.docker.com/desktop/install/windows-install/)
  
#### Exposed REST apis
Here below are the exposed REST Apis:

* CRUD operation for movie reviews 
* CRUD operation for event reviews

## REST apis details
Using a browser it's possible to interact with the REST apis exposed by this service with Swagger:

http://localhost:8081/reviewservice/swagger-ui.html

![Swagger](https://github.com/nrpndr/review-service/blob/main/swagger-ui.png "Swagger interface")

Another alternative is to use Postman (https://www.postman.com/).

This project contains also the [Postman export file](https://github.com/nrpndr/review-service/blob/main/ReviewService.postman_collection.json) with all the configured test calls:

![Postman](https://github.com/nrpndr/review-service/blob/main/postman-ui.png "Postman Collection")

### Running the project
- To run the program, execute below commands in terminal at root level
	
    ```
    mvn clean install
    java -jar target/user-service.jar
    ```
- Prerequisite to the above is that you need a have a local installation of mongodb. [User service](https://github.com/nrpndr/user-service) is required to perform create/update/delete operation.
- If you want to avoid all that hassle, simply do the following(You need to have docker desktop installed for this)
	
    ```
    mvn clean install
    docker compose up
    ```

## Acknowledgments
- [Baeldung](https://www.baeldung.com)
- [StackoverFlow](https://stackoverflow.com/)
- [Mkyong](https://mkyong.com/)
- [spring.io](https://spring.io/)
- [Docker](https://docs.docker.com/)
- [Kubernetes](https://kubernetes.io/)
- [Springdoc](https://springdoc.org/)
- [Swagger](https://swagger.io/)
- [Mongodb](https://www.mongodb.com/)