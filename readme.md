## Evservice
Веб сервис для регистрации пользователей и просмотра баланса,
 сервер на ratpack.

### Запуск

 mvn package   
 java -jar target/ev-service-1.0.jar \<jdbcUrl> \<login> \<pass>

### Параметры
* jdbcUrl - url формата jdbc:postgresql://localhost:5432/dbname
* login - логин для подключения к бд.
* pass - пароль от бд.
