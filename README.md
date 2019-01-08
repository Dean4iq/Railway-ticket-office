# Финальный проект “Железнодорожная касса”

## Вариант 22. Система железнодорожная касса
	Пассажир делает Заявку на билет до необходимой ему станции назначения, время и дату поездки. Система осуществляет поиск подходящего Поезда. Пассажир делает выбор Поезда и получает Счет на оплату. Администратор управляет списком зарегистрированных пассажиров.

### Установка
**Вариант установки 1**

	1.	На странице https://github.com/Dean4iq/Railway-ticket-office выбрать опцию “Clone or download” и скачать ZIP архив проекта.
	2.	После загрузки архива извлечь корневую папку в архиве в любом месте локального хранилища.
	3.	Убедиться, что на устройстве установлены JDK минимум 8 версии и Maven 3.
	4.	Для удобства следует воспользоваться интегрированным средством разработки (IDE), например, Intellij Idea, с помощью которого можно управлять проектом.
	
**Вариант установки 2**

	1.	Убедиться, что на устройстве установлены JDK минимум 8 версии и Maven 3.
	2.	На странице https://github.com/Dean4iq/Railway-ticket-office выбрать опцию “Clone or download” и выбрать Open in Desktop.
	3.	Для успешного клонирования проекта, необходимо заранее загрузить GitHub Desktop и войти под своей учетной записью.
	4.	В открывшемся окне выбрать путь в локальном хранилище, где будет храниться проект. После этого, нажать Clone.
	5.	Все файлы репозитория будут загружены в директорию, которая была выбрана на предыдущем этапе.
	6.	Для удобства следует воспользоваться интегрированным средством разработки (IDE), например, Intellij Idea, с помощью которого можно управлять проектом.

### Обязательно
Установить СУБД **MySQL Server 5.7** и загрузить sql скрипт дампа (**__finalproject_database_dump.sql__**) в БД.
	
### Запуск
**Вариант запуска 1**

	1.	С помощью Intellij Idea (далее IDE) открыть данный проект.
	2.	В IDE в верхнем правом углу нажать “Add configuration”.
	3.	В открывшемся окне выбрать в левом верхнем углу +, после чего в выпадающем списке следует выбрать Maven.
	4.	В правой части окна задать следующие параметры:
		•	Name можно задать любое;
		•	В Working directory должен быть задан абсолютный путь размещения проекта;
		•	В Command line ввести “tomcat7:run”.
	5.	Внизу, в разделе Before launch, нажать на + и выбрать Run Maven Goal.
	6.	В открывшемся окне в поле “Command line” ввести “clean”. Нажать ОК во всех диалоговых окнах.
	7.	Запустить проект с помощью комбинации клавиш Shift+F10 или нажать на зеленый треугольник сверху справа
	8.	После загрузки перейти в веб-браузере по адресу http://localhost:19999/zhd.ua
	
**Вариант запуска 2**

	1.	Запустить командную строку и в ней перейти в корневую папку проекта.
	2.	Ввести команду “mvn clean tomcat7:run”.
	3.	Если команды mvn не обнаружено, то следует указать в переменные среды Path путь к установленному Maven, после чего попытаться запустить проект снова.
