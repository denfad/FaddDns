# Простейший DNS сервер
## Оформление файла конфигурации

```text
example.com 127.0.0.1
# комментарии разрешены ya.ru 127.0.0.1
```

## Сборка
```shell
mvn clean package
```

## Запуск
```shell
java -jar target/FaddDns-1.0.jar src/main/resources/config.txt
```