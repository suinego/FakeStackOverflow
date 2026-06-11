# FakeStackOverflow

Клиент Stack Overflow под Android. Тянет вопросы и ответы по тегу `android` через
Stack Exchange API, складывает их в локальную базу и работает как онлайн, так и без сети.

## Демонстрация

<p align="center">
  <img src="docs/demo.gif" alt="Демонстрация работы приложения" width="320"/>
</p>

## Что умеет

- Список вопросов с тегом `android`, отсортированных по количеству голосов
- Экран с ответами: видно принятый ответ и счётчик голосов
- Онлайн: данные берутся из сети и попутно кешируются
- Офлайн: если сети нет, показывается то, что уже было загружено
- Обновление списка свайпом сверху вниз
- Аватары авторов

## Стек

- Kotlin
- MVVM
- Hilt для внедрения зависимостей
- Retrofit + OkHttp + Moshi для сети
- Room для кеша
- Coroutines и Flow
- View Binding, RecyclerView, Navigation Component, SwipeRefreshLayout
- Coil для картинок

## Онлайн и офлайн

Логика лежит в [StackRepository](app/src/main/java/com/example/fakestackoverflow/data/repository/StackRepository.kt),
наличие сети проверяет [NetworkUtils](app/src/main/java/com/example/fakestackoverflow/utils/NetworkUtils.kt):

- сеть есть — запрос к API, результат пишется в Room и отдаётся из кеша;
- сеть отвалилась посреди запроса — отдаётся последнее, что было сохранено;
- сети нет — данные читаются прямо из Room, а если кеш пустой, возвращается ошибка.

## Сборка

Нужны Android Studio, JDK 11 и Android SDK (compileSdk 36, minSdk 24).

```bash
git clone <repository-url>
cd FakeStackOverflow
./gradlew assembleDebug
```

Или просто открыть проект в Android Studio и запустить конфигурацию `app`.

## Структура

```
app/src/main/java/com/example/fakestackoverflow/
├── data/
│   ├── local/        Room: Entity, DAO, база
│   ├── remote/       Retrofit API, сетевые модели, NetworkModule
│   ├── repository/   StackRepository, логика онлайн/офлайн
│   └── Mapper.kt     сетевые модели -> Entity
├── model/            UI-модели
├── ui/
│   ├── questions/    список вопросов и детали
│   └── answer/       адаптер ответов
├── utils/            NetworkUtils
└── MainActivity.kt
```
