#Base setup

java 1.7
maven 3.3.1

#for installing package


mvn clean install -Dmaven.test.skip=true

#for server running

mvn ninja:run

#for using the api

follow instructions in file apiFiringFormat.txt