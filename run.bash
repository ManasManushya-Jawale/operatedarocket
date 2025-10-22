./gradlew bootJar
java --enable-native-access=ALL-UNNAMED -Xmx1024m -Xms512m -jar "build/libs/operatedarocket-0.0.1-SNAPSHOT.jar"