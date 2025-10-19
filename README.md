# FadDNS

**Самописный DNS-сервер на Java.**

## Конфигурирование

> Основной файл конфигурации - _application.yml_

### Протоколы

#### UDP
```yaml
handlers:
  udp:
    port: 53
    threadsPoolSize: 10
```

#### TLS
```yaml
handlers:
  tls:
    port: 853
    threadsPoolSize: 10
    keyStorePath: "{YOUR JKS KEY STORE}"
    keyStorePassword: "{YOUR JKS KEY STORE PASS}"
    keyPassword: "{YOUR KEY PASS}"
```
### Хранилище записей
Поддерживаемые типы:
- A
- AAAA

Заполнение хранилища:
```yaml
records:
  values:
    -
      domain: example.com # Доменное имя
      type: A # Тип
      values:
        - 127.0.0.1 # Список значений
    - domain: example.com # Доменное имя
      type: AAAA # Тип
      values: # Список значений
        - 2001:0db8:85a3:0000:0000:8a2e:0370:7334
        - 2001:0db8:85a3:0000:0000:8a2e:0370:7334
```

### Вышестоящие DNS-серверы
DNS-сервер позволяет указать вышестоящие сервера, которые будут использоваться в случае отсутствия нужной записи в локальном хранилище

```yaml
upstreams:
  values:
    -
      address: 8.8.8.8
      port: 53
    -
      address: 1.1.1.1
      port: 53
```

## Сборка
```shell
mvn clean package
```

## Запуск
```shell
java -jar target/FaddDns-1.0.jar src/main/resources/application.yml
```