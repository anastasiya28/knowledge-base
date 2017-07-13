INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_USER');

INSERT INTO users (name, password, role_id) VALUES ('ivan','ivan', 2);
INSERT INTO users (name, password, role_id) VALUES ('anna','anna', 2);
INSERT INTO users (name, password, role_id) VALUES ('kate','kate', 2);
INSERT INTO users (name, password, role_id) VALUES ('anton','anton', 2);
INSERT INTO users (name, password, role_id) VALUES ('victor','victor', 1);


INSERT INTO section (parent_id, name) VALUES (null, 'Root');

    INSERT INTO section (parent_id, name) VALUES (1, 'Типы данных, переменные и массивы');
    INSERT INTO section (parent_id, name) VALUES (1, 'Управляющие операторы');
    INSERT INTO section (parent_id, name) VALUES (1, 'Классы. Объектно-ориентированное программирование');
    INSERT INTO section (parent_id, name) VALUES (1, 'Обработка исключений');
    INSERT INTO section (parent_id, name) VALUES (1, 'Коллекции');
    INSERT INTO section (parent_id, name) VALUES (1, 'Потоки ввода-вывода. Работа с файлами');
    INSERT INTO section (parent_id, name) VALUES (1, 'Работа со строками');
    INSERT INTO section (parent_id, name) VALUES (1, 'Обобщения (дженерики)');
    INSERT INTO section (parent_id, name) VALUES (1, 'Лямбда-выражения');
    INSERT INTO section (parent_id, name) VALUES (1, 'Многопоточное программирование');
    INSERT INTO section (parent_id, name) VALUES (1, 'Stream API');

        INSERT INTO section (parent_id, name) VALUES (2, 'Типы данных');
        INSERT INTO section (parent_id, name) VALUES (2, 'Переменные');
        INSERT INTO section (parent_id, name) VALUES (2, 'Массивы');

        INSERT INTO section (parent_id, name) VALUES (3, 'Операторы условия');
        INSERT INTO section (parent_id, name) VALUES (3, 'Операторы цикла');
        INSERT INTO section (parent_id, name) VALUES (3, 'Операторы перехода');

        INSERT INTO section (parent_id, name) VALUES (4, 'Основы классов');
        INSERT INTO section (parent_id, name) VALUES (4, 'Введение в методы');
        INSERT INTO section (parent_id, name) VALUES (4, 'Конструкторы');
        INSERT INTO section (parent_id, name) VALUES (4, 'Модификаторы доступа и инкапсуляция');
        INSERT INTO section (parent_id, name) VALUES (4, 'Наследование. Ключевое слово this и super');
        INSERT INTO section (parent_id, name) VALUES (4, 'Абстрактные классы');
        INSERT INTO section (parent_id, name) VALUES (4, 'Интерфейсы');
        INSERT INTO section (parent_id, name) VALUES (4, 'Переопределение и перегрузка методов. Полиморфизм');


        INSERT INTO section (parent_id, name) VALUES (5, 'Иерархия исключений');
        INSERT INTO section (parent_id, name) VALUES (5, 'Основы обработки исключений. Операторы: try, catch, throw, throws и finally');
        INSERT INTO section (parent_id, name) VALUES (5, 'Создание своих классов исключений');


        INSERT INTO section (parent_id, name) VALUES (6, 'Введение в коллекции');
        INSERT INTO section (parent_id, name) VALUES (6, 'Базовый интерфейс Iterable');
        INSERT INTO section (parent_id, name) VALUES (6, 'Базовый интерфейс Collection и расширяющие его интерфейс List, интерфейс Set, интерфейс Quenue и другие');
        INSERT INTO section (parent_id, name) VALUES (6, 'ArrayList и LinkedList - основные реализации интерфейса List');
        INSERT INTO section (parent_id, name) VALUES (6, 'HashSet, LinkedHashSet и TreeSet - основные реализации интерфейса Set');
        INSERT INTO section (parent_id, name) VALUES (6, 'Базовый интерфейс Map');
        INSERT INTO section (parent_id, name) VALUES (6, 'HashMap, LinkedHashMap, TreeMap - основные реализации интерфейса Map');
        INSERT INTO section (parent_id, name) VALUES (6, 'Потокобезопасные коллекции');


        INSERT INTO section (parent_id, name) VALUES (7, 'Основные предки символьных потоков ввода/вывода: Reader, Writer');
        INSERT INTO section (parent_id, name) VALUES (7, 'Основные предки байтовых потоков ввода/вывода: InputStream, OutputStream');
        INSERT INTO section (parent_id, name) VALUES (7, 'Буферизируемые символьные потоки. BufferedReader и BufferedWriter');
        INSERT INTO section (parent_id, name) VALUES (7, 'Буферизуемые потоки. Классы BufferedInputStream и BufferedOuputStream');
        INSERT INTO section (parent_id, name) VALUES (7, 'Чтение и запись текстовых файлов. FileReader и FileWriter');
        INSERT INTO section (parent_id, name) VALUES (7, 'Чтение и запись файлов. FileInputStream и FileOutputStream');
        INSERT INTO section (parent_id, name) VALUES (7, 'Сериализация объектов');
        INSERT INTO section (parent_id, name) VALUES (7, 'Класс File. Работа с файлами и каталогами');


        INSERT INTO section (parent_id, name) VALUES (8, 'Строки. Основные операции со строками');
        INSERT INTO section (parent_id, name) VALUES (8, 'Классы StringBuffer и StringBuilder');


        INSERT INTO section (parent_id, name) VALUES (11, 'Способы создания и запуска потоков: с использованием класса Thread, либо реализуя интерфейс Runnable или интерфейс Callable');
        INSERT INTO section (parent_id, name) VALUES (11, 'Синхронизация потоков. Оператор synchronized');
        INSERT INTO section (parent_id, name) VALUES (11, 'Приостановка, возобновление и остановка потоков исполнения');

               INSERT INTO section (parent_id, name) VALUES (13, 'Примитивные типы данных');
               INSERT INTO section (parent_id, name) VALUES (13, 'Ссылочные типы данных');

                      INSERT INTO section (parent_id, name) VALUES (51, 'Целочисленные типы');
                      INSERT INTO section (parent_id, name) VALUES (51, 'Типы с плавающей точкой');
                      INSERT INTO section (parent_id, name) VALUES (51, 'Логический тип');


INSERT INTO article (name, title, section_id) VALUES('data_types.md', 'Типы данных в Java', 13);
INSERT INTO article (name, title, section_id) VALUES('primitive_data_types.md', 'Примитивные типы данных', 51);
INSERT INTO article (name, title, section_id) VALUES('reference_data_types.md', 'Ссылочные типы данных', 52);
INSERT INTO article (name, title, section_id) VALUES('integer_types.md', 'Целочисленные типы', 53);
INSERT INTO article (name, title, section_id) VALUES('floating_point_types.md', 'Типы с плавающей точкой', 54);
INSERT INTO article (name, title, section_id) VALUES('boolean_type.md', 'Логический тип', 55);
INSERT INTO article (name, title, section_id) VALUES('variables.md', 'Переменные', 14);
INSERT INTO article (name, title, section_id) VALUES('constants.md', 'Константы', 14);
INSERT INTO article (name, title, section_id) VALUES('arrays.md', 'Массивы', 15);


INSERT INTO tag (name) VALUES ('примитивные типы');
INSERT INTO tag (name) VALUES ('byte');
INSERT INTO tag (name) VALUES ('short');
INSERT INTO tag (name) VALUES ('char');
INSERT INTO tag (name) VALUES ('int');
INSERT INTO tag (name) VALUES ('long');
INSERT INTO tag (name) VALUES ('float');
INSERT INTO tag (name) VALUES ('double');
INSERT INTO tag (name) VALUES ('boolean');
INSERT INTO tag (name) VALUES ('ссылочные типы');
INSERT INTO tag (name) VALUES ('класс обертка');
INSERT INTO tag (name) VALUES ('boxing');
INSERT INTO tag (name) VALUES ('unboxing');
INSERT INTO tag (name) VALUES ('переменная');
INSERT INTO tag (name) VALUES ('массив');
INSERT INTO tag (name) VALUES ('одномерный массив');
INSERT INTO tag (name) VALUES ('многомерный массив');
INSERT INTO tag (name) VALUES ('операторы условия');
INSERT INTO tag (name) VALUES ('оператор if');
INSERT INTO tag (name) VALUES ('оператор swith');
INSERT INTO tag (name) VALUES ('операторы_цикла');
INSERT INTO tag (name) VALUES ('цикл while');
INSERT INTO tag (name) VALUES ('цикл do-while');
INSERT INTO tag (name) VALUES ('цикл for');
INSERT INTO tag (name) VALUES ('операторы перехода');
INSERT INTO tag (name) VALUES ('оператор break');
INSERT INTO tag (name) VALUES ('оператор continue');
INSERT INTO tag (name) VALUES ('оператор return');
INSERT INTO tag (name) VALUES ('класс');
INSERT INTO tag (name) VALUES ('поля класса');
INSERT INTO tag (name) VALUES ('методы класса');
INSERT INTO tag (name) VALUES ('конструктор');


INSERT INTO article_tag VALUES(1, 1);
INSERT INTO article_tag VALUES(1, 10);
INSERT INTO article_tag VALUES(1, 2);
INSERT INTO article_tag VALUES(1, 3);
INSERT INTO article_tag VALUES(1, 4);
INSERT INTO article_tag VALUES(1, 5);
INSERT INTO article_tag VALUES(1, 6);
INSERT INTO article_tag VALUES(1, 7);
INSERT INTO article_tag VALUES(1, 8);
INSERT INTO article_tag VALUES(1, 9);

INSERT INTO article_tag VALUES(2, 1);
INSERT INTO article_tag VALUES(2, 2);
INSERT INTO article_tag VALUES(2, 3);
INSERT INTO article_tag VALUES(2, 4);
INSERT INTO article_tag VALUES(2, 5);
INSERT INTO article_tag VALUES(2, 6);
INSERT INTO article_tag VALUES(2, 7);
INSERT INTO article_tag VALUES(2, 8);
INSERT INTO article_tag VALUES(2, 9);

INSERT INTO article_tag VALUES(4, 2);
INSERT INTO article_tag VALUES(4, 3);
INSERT INTO article_tag VALUES(4, 4);
INSERT INTO article_tag VALUES(4, 5);
INSERT INTO article_tag VALUES(4, 6);

INSERT INTO article_tag VALUES(5, 7);
INSERT INTO article_tag VALUES(5, 8);

INSERT INTO article_tag VALUES(6, 9);

INSERT INTO article_tag VALUES(4, 11);
INSERT INTO article_tag VALUES(5, 11);
INSERT INTO article_tag VALUES(6, 11);

INSERT INTO article_tag VALUES(3, 10);

INSERT INTO article_tag VALUES(7, 14);

INSERT INTO article_tag VALUES(9, 15);
INSERT INTO article_tag VALUES(9, 16);
INSERT INTO article_tag VALUES(9, 17);