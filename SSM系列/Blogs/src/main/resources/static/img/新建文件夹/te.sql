drop database if exists te;
CREATE SCHEMA `te` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
use te;
create table user(
	id int,
    name varchar(45),
    age int,
    class_id int,
    address_id int
);
create table class(
	id int,
    name varchar(45)
);
create table address(
	id int,
    name varchar(45)
);

INSERT INTO user VALUES (1,'赵二',15,1,1);
INSERT INTO user VALUES (1,'张三',16,2,2);
INSERT INTO user VALUES (1,'赵四',17,1,1);
INSERT INTO user VALUES (1,'王五',18,2,3);

INSERT INTO class VALUES (1,'计卓1班');
INSERT INTO class VALUES (2,'计卓2班');
INSERT INTO address VALUES (1,'陕西');
INSERT INTO address VALUES (2,'北京');
INSERT INTO address VALUES (3,'上海');