# Galaxy

Тестовое задание по теме «Объектно-ориентированное программирование на языке Java с применением технологий работы с XML».

Указания к выполнению задания:
1.	Все классы должны удовлетворять Code Conventions for the Java Programming Language и принципам SOLID
2.	В каждом классе должны быть описаны конструкторы по умолчанию, конструкторы с параметрами, инициализирующими поля классов, методы получения и установки значений в каждое из полей класса, метод toString, методы hashCode и equals

Задание:
1. Описать понятие планета, поля (минимум название планеты) и метод поведения планеты(behavior) определить самостоятельно

2. Описать понятие галактика, поля (минимум название галактики и список планет для текущей галактики) и метод поведения галактики(behavior – должен обращаться к каждой планете в галактике) определить самостоятельно
2.1. Реализовать метод добавления новой планеты в список всех планет текущей галактики
2.2. Реализовать методы поиска планеты по ее имени в галактике и по объекту самой планеты
2.3. Реализовать методы удаления планеты из галактики по ее имени и по объекту самой планеты

3. Описать понятие вселенная,  поля (минимум список галактик для текущей вселенной) определить самостоятельно
3.1. Во вселенной реализовать метод добавления новой галактики в список всех галактик
3.2. Реализовать методы поиска планеты из вселенной (по имени и по объекту), метод поиска галактики из вселенной (по имени и по объекту).
3.3 Метод поведение, определить как генерацию случайным образом раз в 30 секунд случайного количества галактик со случайным числом планет, имена галактик и планет генерировать случайным образом (реализовать для этого отдельный класс со специализирующими методами): имя планеты должно начинаться с буквы Р, далее идет последовательность цифр , имя галактики аналогично , начиная с G. 

Задание на разбор файлов XML:
1.	Для каждого класса реализовать методы загрузки данных из XML файла и выгрузку в него, структура которого должна соответствовать структуре класса. При реализации данных методов использовать DOM-Parser с применением технологии XPath.
2.	Для класса Вселенной произвести трансформацию сгенерированного файла .xml по следующему признаку:
•	Все планеты с одинаковыми именами группируются в теги
 <EqualGroup number=”i” name=””>…</ EqualGroup>, где number  - это номер тега в файле, name – имя группы тегов, объединенных по данному имени  
•	Все теги EqualGroup размещены в корневом теге Planets
