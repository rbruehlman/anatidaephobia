FROM theasp/clojurescript-nodejs:alpine as build
WORKDIR /app
COPY package.json package-lock.json yarn.lock project.clj ./

RUN npm install

COPY . .

RUN lein uberjar && mv /app/target/thagomizer.jar ./thagomizer.jar

EXPOSE 5000

CMD ["java", "-jar", "thagomizer.jar"]