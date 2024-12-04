![Bot](https://github.com/sanyarnd/java-course-2023-backend-template/actions/workflows/bot.yml/badge.svg)
![Scrapper](https://github.com/sanyarnd/java-course-2023-backend-template/actions/workflows/scrapper.yml/badge.svg)

# Link Tracker

ФИО: Киреев Дмитрий Александрович

Проект состоит из 2-х приложений:
* Bot
* Scrapper

Целью проекта было создание приложения с интерфейсом в виде telegram-бота, которое позволяло бы мониторить изменения по url github репозиториев / stackoverflow вопросов. 
Пользователь может зарегистрироваться (иначе выполнение иных команд невозможно) и добавить в трекинг ссылку\несколько. Бот присылает ему уведомление как только заметит определенные изменения.

Были рассмотрены многочисленные варианты взаимодействия между сервисами (http-запросы, очереди), между сервисами и базой данных (jpa, jdbc), реализованы метрики, retry-механизмы, использовались базовые шаблоны проектирования, покрытие unit и интеграционными тестами.

Стек:
Язык:
●	Java
Фреймворки и библиотеки:
●	Spring Boot
●	Spring Data JPA
●	Jdbc Driver
●	Retry mechanisms
База данных:
●	PostgreSQL
●	Liquibase
Очередь сообщений:
●	Kafka

Документация:
●	Swagger

Контейнеризация:
●	Docker
Система контроля версий:
●	Git
Сборка:
●	Maven

Тестирование:
●	JUnit
●	Mockito
●	Spring Test


Метрики:
●	Prometheus 
●	Grafana


