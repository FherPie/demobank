#FROM adoptopenjdk:11-jre-hotspot
#copy ./target/demo-1.jar demo-1.jar
#CMD ["java","-jar","demo-1.jar"]


#docker build -t fherpie/demo . 

#docker run -p 8080:8080 fherpie/demo   
#docker container run --network clients-mysql --name clients-container -p 8080:8080 -d fherpie/demo


#docker network create clients-mysql

#docker container run --name mysqldb -e MYSQL_ROOT_PASSWORD=root --network clients-mysql -e MYSQL_DATABASE=testdb -d mysql:8

FROM adoptopenjdk:11-jre-hotspot
RUN mkdir -p /usr/demo/bin /usr/demo/logs \
		&& chmod -R 775 /usr/demo
WORKDIR /usr/demo
COPY target/demo-1.jar .
COPY src/main/resources/application.properties /usr/demo/bin/
RUN chmod 775 demo-1.jar
CMD ["java", "-jar", "demo-1.jar","--spring.config.location=file:///usr/demo/bin/application.properties"]
EXPOSE 8080


#docker build -t demo-boot .

#docker run -p 8080:8080 demo-boot

#docker run -p 4200:80 demo-boot