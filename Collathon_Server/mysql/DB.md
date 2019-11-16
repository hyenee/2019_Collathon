# MYSQL
> We used 'mysql' as our database.

## 1. CREATE DATABASE
> Our database name is 'collathon'
```sql
create database collathon;
```

## 2. CREATE TABLES
### (1) Table: Client
```sql
create table Client(
name varchar(15) NOT NULL COMMENT '고객 이름',
id varchar(100) NOT NULL COMMENT '고객 아이디',
passwd varchar(200) NOT NULL COMMENT '고객 비밀번호',
phone varchar(11) NOT NULL COMMENT '고객 전화번호',
email varchar(50) DEFAULT NULL COMMENT '고객 이메일',
PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8; 
```

### (2) Table: Supplier
```sql
create table Client(
name varchar(15) NOT NULL COMMENT '사장 이름',
id varchar(100) NOT NULL COMMENT '사장 아이디',
passwd varchar(200) NOT NULL COMMENT '사장 비밀번호',
phone varchar(11) NOT NULL COMMENT '사장 전화번호',
PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8; 
```

### (3) Table: Shop
```sql
create table Shop(
id int(11) NOT NULL AUTO_INCREMENT COMMENT '가게 고유번호',
master varchar(100) NOT NULL COMMENT '가게 주인 아이디',
name varchar(50) NOT NULL COMMENT '가게 이름',
tel varchar(11) NOT NULL COMMENT '가게 전화번호',
address varchar(50) NOT NULL COMMENT '가게 주소',
category varchar(20) NOT NULL COMMENT '가게 카테고리',
check_table varchar(2) NOT NULL COMMENT '가게 테이블 여부',
PRIMARY KEY (id,master),
KEY master (master),
CONSTRAINT Shop_ibfk_1 FOREIGN KEY (master) REFERENCES Supplier (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8; 
```

### (4) Table: ShopTable
```sql
create table ShopTable(
shop_id int(11) NOT NULL DEFAULT 0,
number int(11) NOT NULL COMMENT '테이블 정원',
count int(11) NOT NULL COMMENT '테이블 수',
PRIMARY KEY (shop_id,number),
CONSTRAINT ShopTable_ibfk_1 FOREIGN KEY (shop_id) REFERENCES Shop (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8; 
```

### (5) Table: Menu
```sql
create table Menu(
shop_id int(11) NOT NULL COMMENT '가게 번호',
name varchar(45) NOT NULL COMMENT '메뉴 이름',
price int(11) NOT NULL COMMENT '메뉴 가격',
description varchar(500) DEFAULT NULL COMMENT '메뉴 설명',
count int(11) DEFAULT NULL COMMENT '메뉴 재고 수',
PRIMARY KEY (shop_id,name),
CONSTRAINT Menu_ibfk_1 FOREIGN KEY (shop_id) REFERENCES Shop (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8; 
```

### (6) Table: BlackList
```sql
create table BlackList(
client_id varchar(100) NOT NULL COMMENT '고객 아이디',
shop_id int(11) NOT NULL COMMENT '가게 번호',
comment varchar(500) DEFAULT NULL COMMENT '코멘트(설명)',
PRIMARY KEY (client_id,shop_id),
KEY shop_id (shop_id),
CONSTRAINT BlackList_ibfk_1 FOREIGN KEY (client_id) REFERENCES Client (id),
CONSTRAINT BlackList_ibfk_2 FOREIGN KEY (shop_id) REFERENCES Shop (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8; 
```

### (7) Table: Likes
```sql
create table Likes(
shop_id int(11) NOT NULL COMMENT '가게 번호',
name varchar(100) NOT NULL COMMENT '고객 아이디',
PRIMARY KEY (shop_id,name),
KEY name (name),
CONSTRAINT Likes_ibfk_1 FOREIGN KEY (name) REFERENCES Client (id),
CONSTRAINT Likes_ibfk_2 FOREIGN KEY (shop_id) REFERENCES Shop (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8; 
```

### (8) Table: Reservation
```sql
create table Reservation( 
id int(11) NOT NULL AUTO_INCREMENT COMMENT '예약번호',
classification varchar(12) DEFAULT NULL,
client_id varchar(100) NOT NULL COMMENT '고객 아이디',
time varchar(11) NOT NULL COMMENT '예약시간(HH:mm-HH:mm)',
shop_id int(11) NOT NULL COMMENT '가게 번호',
PRIMARY KEY (id),
KEY client_id (client_id),
KEY shop_id (shop_id),
CONSTRAINT Reservation_ibfk_1 FOREIGN KEY (client_id) REFERENCES Client (id),
CONSTRAINT Reservation_ibfk_2 FOREIGN KEY (shop_id) REFERENCES Shop (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8; 
```

### (9) Table: ReservationTable
```sql
create table ReservationTable( 
id int(11) NOT NULL COMMENT '예약번호',
shop_id int(11) NOT NULL COMMENT '가게 번호',
number int(11) NOT NULL COMMENT '예약 테이블 정원',
reservation_count int(11) NOT NULL COMMENT '예약 테이블 개수',
PRIMARY KEY (id,number),
KEY shop_id (shop_id,number),
CONSTRAINT ReservationTable_ibfk_1 FOREIGN KEY (id) REFERENCES Reservation (id),
CONSTRAINT ReservationTable_ibfk_2 FOREIGN KEY (shop_id, number) REFERENCES ShopTable (shop_id, number)
)ENGINE=INNODB DEFAULT CHARSET=utf8; 
```

### (10) Table: ReservationMenu
```sql
create table ReservationMenu( 
id int(11) NOT NULL COMMENT '예약번호',
shop_id int(11) NOT NULL COMMENT '가게 번호',
name varchar(45) NOT NULL COMMENT '예약 메뉴 이름',
count int(11) NOT NULL COMMENT '예약 메뉴 개수',
PRIMARY KEY (id,name),
KEY shop_id (shop_id,name),
CONSTRAINT ReservationMenu_ibfk_1 FOREIGN KEY (id) REFERENCES Reservation (id),
CONSTRAINT ReservationMenu_ibfk_2 FOREIGN KEY (shop_id, name) REFERENCES Menu (shop_id, name)
)ENGINE=INNODB DEFAULT CHARSET=utf8; 
```
### (11) Table: TimeSale
```sql
create table TimeSale(
id int(11) NOT NULL AUTO_INCREMENT COMMENT '타임세일번호',
shop_id int(11) NOT NULL COMMENT '가게 번호',
name varchar(45) NOT NULL COMMENT '세일 메뉴 이름',
sale_price int(11) NOT NULL COMMENT '세일 가격',
time varchar(12) NOT NULL COMMENT '세일 시간',
PRIMARY KEY (id),
KEY shop_id (shop_id,name),
CONSTRAINT TimeSale_ibfk_1 FOREIGN KEY (shop_id, name) REFERENCES Menu (shop_id, name)
)ENGINE=INNODB DEFAULT CHARSET=utf8; 
```
