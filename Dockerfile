FROM clojure:openjdk-11-lein-buster

RUN apt-get update && -y install npm && nodejs

RUN mkdir -p /usr/thagomizer/
WORKDIR /usr/thagomizer/
COPY project.clj .
RUN lein deps
RUN npm install

COPY . .

RUN lein prod
RUN mv "$(lein uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" app-standalone.jar

EXPOSE 5000

CMD ["java", "-jar", "app-standalone.jar"]