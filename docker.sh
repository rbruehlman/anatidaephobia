DOCKER_BUILDKIT=1 docker build . -t thagomizer:latest --secret id=aws,src=$HOME/.aws/credentials
docker tag $(docker images --format='{{.ID}}' | head -1) rbruehlman/thagomizer:latest
docker push rbruehlman/thagomizer:latest