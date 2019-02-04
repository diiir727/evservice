## Evservice
Веб сервис для регистрации пользователей и просмотра баланса,
 сервер на ratpack.

### Запуск
 Создайте необходимые таблицы в БД, для этого в корне проекта лежит db_init.sql   
 mvn package   
 java -jar target/ev-service-1.0.jar \<jdbcUrl> \<login> \<pass>
 запросы отправлять на localhost:5050

### Параметры
* jdbcUrl - url формата jdbc:postgresql://localhost:5432/dbname
* login - логин для подключения к бд.
* pass - пароль от бд.
