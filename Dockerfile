FROM theasp/clojurescript-nodejs:alpine as build
WORKDIR /app
COPY package.json package-lock.json yarn.lock project.clj ./

RUN npm install

COPY . .

# hack because clj wanted environment variables to expand, but i got none yet, yo
ENV AWS_ACCESS_KEY="AWS_ACCESS_KEY"
ENV AWS_SECRET_ACCESS_KEY="AWS_SECRET_ACCESS_KEY"
ENV TOPIC_ARN="TOPIC_ARN"

RUN lein uberjar && mv /app/target/thagomizer.jar ./thagomizer.jar

EXPOSE 5000

ENV AWS_REGION="us-east-1"

CMD ["java", "-jar", "thagomizer.jar"]