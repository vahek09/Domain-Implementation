This document provides a guide for setting up a PostgreSQL database using Docker Compose. It includes steps for configuring, running, and verifying the setup.

Prerequisites
Docker: Ensure Docker is installed on your system. Download Docker
Docker Compose: Make sure Docker Compose is installed. It usually comes with Docker Desktop. Install Docker Compose
Setup Instructions
This guide provides instructions on how to set up a local PostgreSQL database using Docker Compose. The setup is managed via a docker-compose.yml file and leverages an .env file for configuration. Update the .env File

In the root directory of your project, create a file named .env with the following content: DB_USER = Your_User_Name DB_PASSWORD = Your_Password DB_NAME = Database name (ex. postgres) DB_PORT= 5432 (by default)

The docker-compose.yml has ready configuration, which leverages an .env file.

Run: docker-compose up -d command Verify the container is running: docker ps

Connect Docker to the database using DBeaver: Host: localhost Port: 5432 Database: (the database name defined in .env) Username: (the username defined in .env) Password: (the password defined in .env)

Stopping and Removing the Container: docker-compose down This will stop the container and remove it, but the data will be preserved in the volume.
