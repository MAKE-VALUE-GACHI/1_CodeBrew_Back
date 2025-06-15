# Doctor Geondam

## Docker Setup

This project includes Docker configuration for easy setup and deployment.

### Prerequisites

- Docker and Docker Compose installed on your machine

### Running with Docker

1. Clone the repository
2. Navigate to the project directory
3. Run the following command to start the application and database:

```bash
docker-compose up -d
```

This will:
- Build the Spring Boot application
- Start a MySQL database
- Connect the application to the database

The application will be available at http://localhost:8080

### Stopping the Application

To stop the containers, run:

```bash
docker-compose down
```

To stop the containers and remove the volumes (this will delete the database data), run:

```bash
docker-compose down -v
```

### Environment Configuration

The Docker setup uses the following configuration:

- MySQL database running on port 3306
- Spring Boot application running on port 8080
- Database name: doctorgeondam
- Database root password: rootpassword

These settings can be modified in the docker-compose.yml file.