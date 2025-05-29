# ............................................................................
# justfile for Spring Boot Project
#
# Use 'just --list' to see available commands.
# ............................................................................

# @desc Builds the entire project, cleaning first. (mvn clean install)
build:
  mvn clean install

# @desc Runs the 'api-service' module specifically using Spring Boot's run command.
run:
  mvn spring-boot:run -pl api-service

# @desc Runs all tests in the project. (mvn test)
test:
  mvn test