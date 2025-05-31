# ............................................................................
# justfile for Spring Boot Project
#
# Use 'just --list' to see available commands.
# ............................................................................

# @desc Builds the entire project, cleaning first. (mvn clean install)
build:
  mvn clean install -DskipTests

# @desc Runs the 'api-service' module specifically using Spring Boot's run command.
runa:
  mvn spring-boot:run -pl api-service

# @desc Runs all tests in the project. (mvn test)
test:
  mvn test

# @desc Build the worker-service:
buildw:
  mvn clean install -pl worker-service -DskipTests

# @desc Run the worker-service locally :
runw:
  mvn spring-boot:run -pl worker-service

#dont worry folks i will be back with a dedicated testing approach later
