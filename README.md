# 🌐 FadDNS

**Самописный DNS-сервер на Java.**


## Характеристики

- **Поддержка протоколов**: UDP, TCP, DNS-over-TLS
- **Хранилище записей**: Поддержка A и AAAA записи
- **Рекурсивные запросы**: Интеграция с вышестоящими DNS-серверами
- **Обработка**: возможность настройки многопоточной обработки

## Быстрый старт

## Сборка проекта
```shell
mvn clean package
```

## Запуск
```shell
java -jar target/FaddDns-1.0.jar src/main/resources/application.yml
```


## Конфигурация

Основной файл конфигурации - `application.yml`

### Настройка протоколов

#### UDP
```yaml
handlers:
  udp:
    port: 53 # Стандартный порт DNS
    threadsPoolSize: 10 # Размер пула потоков
```

#### TCP
```yaml
handlers:
  tcp:
    port: 53 # Стандартный порт TCP
    threadsPoolSize: 10 # Размер пула потоков
```

#### TLS
```yaml
handlers:
  tls:
    port: 853 # Стандартный порт DoT
    threadsPoolSize: 10 # Размер пула потоков
    keyStorePath: "{YOUR JKS KEY STORE}"
    keyStorePassword: "{YOUR JKS KEY STORE PASS}"
    keyPassword: "{YOUR KEY PASS}"
```
### Настройка хранилища записей
Поддерживаемые типы:
- A (ipv4)
- AAAA (ipv6)

Заполнение хранилища:
```yaml
records:
  values:
    -
      domain: fadeev.com # Доменное имя
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
Настройте резервные DNS-серверы для обработки запросов, отсутствующих в локальном хранилище:

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
