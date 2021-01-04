# syntax=docker/dockerfile:experimental

FROM theasp/clojurescript-nodejs:alpine as build
WORKDIR /app
COPY package.json package-lock.json yarn.lock project.clj ./

RUN npm install

COPY . .

ENV AWS_PROFILE=thagomizer
ENV AWS_REGION="us-east-1"

RUN --mount=type=secret,id=aws,target=/root/.aws/credentials lein uberjar

RUN mv /app/target/thagomizer.jar ./thagomizer.jar

EXPOSE 8000

CMD ["java", "-jar", "thagomizer.jar"]

#so i don't forget later
#DOCKER_BUILDKIT=1 docker build . -t thagomizer:latest --secret id=aws,src=$HOME/.aws/credentials
#docker run -p 80:8000 -v ~/.aws/:/root/.aws/ -e DB_HOST="" -e DB_PASSWORD="" rbruehlman/thagomizer:latest