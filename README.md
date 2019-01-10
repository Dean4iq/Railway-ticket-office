# Final project “Railway ticket office”

## Variant 22. System "Railway ticket office"
	The passenger makes an application for a ticket to the destination station he needs, time and date of travel. The system searches for a suitable train. The passenger makes the choice of a train and receives an invoice. The administrator manages the list of registered passengers.

### Installation
**Installation option 1**

	1.	On the page https://github.com/Dean4iq/Railway-ticket-office select the option “Clone or download” and download the ZIP archive of the project.
	2.	After downloading the archive, extract the root folder in the archive anywhere in the local storage.
	3.	Ensure that at least 8 versions of JDK and Maven 3 are installed on the device.
	4.	For convenience, you should use the integrated development tool (IDE), for example, Intellij Idea, with which you can manage the project.
	
**Installation option 2**

	1.	Ensure that at least 8 versions of JDK and Maven 3 are installed on the device.
	2.	On the page https://github.com/Dean4iq/Railway-ticket-office select the option “Clone or download” and select Open in Desktop.
	3.	To successfully clone a project, you need to preload GitHub Desktop and log in with your account.
	4.	In the window that opens, select the path in the local storage where the project will be stored. After that, click Clone.
	5.	All repository files will be uploaded to the directory that was selected in the previous step.
	6.	For convenience, you should use the integrated development tool (IDE), for example, Intellij Idea, with which you can manage the project.

### Mandatory
Install the DBMS **MySQL Server 5.7** and load the sql dump script (**__finalproject_database_dump.sql__**) into the database.
	
### Starting project
**Startup option 1**

	1.	Using Intellij Idea (hereinafter IDE) to open this project.
	2.	In the IDE in the upper right corner, click “Add configuration”.
	3.	In the window that opens, select + in the upper left corner, then select Maven in the drop-down list.
	4.	In the right part of the window, set the following parameters:
		•	The Name can be any;
		•	In the Working directory, the absolute location of the project should be specified;
		•	In the command line, enter “tomcat7: run”.
	5.	At the bottom, in the Before launch section, click on + and select Run Maven Goal.
	6.	In the window that opens, enter “clean” in the “Command line” field. Click OK in all dialog boxes.
	7.	Run the project using the Shift + F10 key combination or click on the green triangle from the top right
	8.	After downloading, go to the web browser at http://localhost:19999/zhd.ua
	
**Startup option 2**

	1.	Run the command line and go to the project root folder in it.
	2.	Enter the command “mvn clean tomcat7: run”.
	3.	If the mvn command is not detected, then the path to the installed Maven should be specified in Path environment variables, then try to start the project again.


# Финальный проект “Железнодорожная касса”

## Вариант 22. Система "Железнодорожная касса"
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
