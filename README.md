#  Gradle bootstrap project.

Built using the following commands.

    gradle wrapper --gradle-version=4.2.1
    ./gradlew init --type java-application --test-framework spock


To run:

    ./gradlew clean build shadowJar
    java -jar build/libs/foo-all.jar
    
    # or
    ./gradlew clean build runShadow

