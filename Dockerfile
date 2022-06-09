FROM adoptopenjdk:11-jre-hotspot
copy ./target/demo-1.jar demo-1.jar
CMD ["java","-jar","demo-1.jar"]


#docker build -t fherpie/demo . 

#docker run -p 8080:8080 fherpie/demo   docker container run --network clients-mysql --name clients-container -p 8080:8080 -d fherpie/demo


#docker network create clients-mysql

#docker container run --name mysqldb -e MYSQL_ROOT_PASSWORD=root --network clients-mysql -e MYSQL_DATABASE=testdb -d mysql:8