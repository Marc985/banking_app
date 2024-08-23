# Étape de construction
FROM eclipse-temurin:22-jdk-jammy AS build

# Définir les variables d'environnement pour Gradle
ENV GRADLE_HOME /opt/gradle
ENV GRADLE_VERSION 8.9
ENV GRADLE_DOWNLOAD_SHA256=d725d707bfabd4dfdc958c624003b3c80accc03f7037b5122c4b1d0ef15cecab

# Installer Gradle
RUN set -o errexit -o nounset \
    && apt-get update \
    && apt-get install --yes --no-install-recommends \
        unzip \
        wget \
    && rm -rf /var/lib/apt/lists/* \
    && wget --no-verbose --output-document=gradle.zip "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" \
    && echo "${GRADLE_DOWNLOAD_SHA256} *gradle.zip" | sha256sum --check - \
    && unzip gradle.zip \
    && rm gradle.zip \
    && mv "gradle-${GRADLE_VERSION}" "${GRADLE_HOME}/" \
    && ln --symbolic "${GRADLE_HOME}/bin/gradle" /usr/bin/gradle \
    && gradle --version

# Copier les fichiers du projet dans le conteneur
COPY . /home/gradle/project
WORKDIR /home/gradle/project

# Construire le projet avec Gradle
RUN gradle build --no-daemon -x test

# Étape de production
FROM eclipse-temurin:22-jdk-jammy

# Copier le JAR construit depuis l'étape de construction
COPY --from=build /home/gradle/project/build/libs/wallet-app-0.0.1-SNAPSHOT.jar wallet-app.jar

# Exposer le port
EXPOSE 8080

# Définir le point d'entrée pour le conteneur
ENTRYPOINT ["java", "-jar", "wallet-app.jar"]
