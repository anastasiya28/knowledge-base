--Типы данных smallserial, serial и bigserial не являются настоящими типами, а представляют собой просто удобное средство
--для создания столбцов с уникальными идентификаторами (подобное свойству AUTO_INCREMENT в некоторых СУБД).

CREATE SEQUENCE role_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE users_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE catalog_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE tag_id_seq START WITH 1 INCREMENT BY 1;



DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS article_tag;
DROP TABLE IF EXISTS article;

-- Дамп таблицы role
-- ------------------------------------------------------------
DROP TABLE IF EXISTS role;
CREATE TABLE role (
  id SERIAL NOT NULL UNIQUE PRIMARY KEY,
  name varchar(250) NOT NULL
);

-- Дамп таблицы users
 ------------------------------------------------------------
CREATE TABLE users (
    id SERIAL NOT NULL UNIQUE PRIMARY KEY,
    name varchar(250) UNIQUE NOT NULL,
    password varchar(250) NOT NULL,
    role_id INTEGER REFERENCES role(id)
  );


DROP TABLE IF EXISTS section;
CREATE TABLE section (
     id SERIAL NOT NULL UNIQUE PRIMARY KEY,
	 parent_id INTEGER REFERENCES section(id),
	 name varchar(2000) NOT NULL
  );

CREATE TABLE article (
      id SERIAL NOT NULL UNIQUE PRIMARY KEY,
      name varchar (2000) NOT NULL,
      title varchar (2000) NOT NULL,
      section_id INTEGER REFERENCES section(id)
  );

DROP TABLE IF EXISTS tag;
CREATE TABLE tag (
      id SERIAL NOT NULL UNIQUE PRIMARY KEY,
      name varchar(250) UNIQUE NOT NULL
  );

 CREATE TABLE article_tag (
   article_id INTEGER REFERENCES article (id),
   tag_id INTEGER REFERENCES tag (id)
 );

