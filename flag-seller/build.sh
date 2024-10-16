#!/bin/bash

# Run the Gradle build
./gradlew clean bootJar

# Check if the Gradle build was successful
if [ $? -eq 0 ]; then
    echo "Gradle build successful. Proceeding with Docker build."

    # Docker image name and version
    IMAGE_NAME="flag-seller"
    IMAGE_VERSION="dev"

    # Check if an image with the same name and version already exists
    EXISTING_IMAGE=$(docker images -q $IMAGE_NAME:$IMAGE_VERSION)

    # If an existing image is found, remove it
    if [[ -n $EXISTING_IMAGE ]]; then
        echo "Removing existing image: $EXISTING_IMAGE"
        docker rmi $EXISTING_IMAGE
    fi

    # Build the Docker image
    docker build -t $IMAGE_NAME:$IMAGE_VERSION .

    # Tag and push the new image
    docker tag $IMAGE_NAME:$IMAGE_VERSION $IMAGE_NAME:$IMAGE_VERSION
    #docker push $IMAGE_NAME:$IMAGE_VERSION

    echo "Docker image build and replacement completed."
    echo "Docker image is => $IMAGE_NAME:$IMAGE_VERSION"
else
    echo "Gradle build failed. Docker build aborted."
    exit 1
fi
