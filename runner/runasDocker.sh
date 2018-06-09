#!/bin/bash
cd ..
mvn clean install package
cp ./target/csvtodb-0.0.1-SNAPSHOT.jar runner/

if [ ! "$(docker ps -q -f name=mysql)" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=mysql)" ]; then
        docker rm mysql
    fi
	docker run --detach --name=test-mysql --env="MYSQL_ROOT_PASSWORD=mypassword" --publish 6603:3306 mysql --max-connections=200 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
	echo "Giving some time to mysql to come to life ... Ideally should be done by checking the port"
	sleep 60
fi


cd runner/

java -jar csvtodb-0.0.1-SNAPSHOT.jar resources.conf