# topjava_docker
springboot idea:
1. run TopjavaDockerApplication
2. открыть http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

springboot postgres docker:
1. установить докер
2. создать БД постгрес topjavadocker, если креденшиалы не user/password то внести соотв изменения:  
   1. src\main\resources\application.properties  
   1. docker-compose.yml  
3. собрать jar 
4. в терминале из корневого каталога "docker-compose up --build"
5. открыть http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

дефолтные тестовые можно получить при авторизации под user@mail.ru (без пароля)
