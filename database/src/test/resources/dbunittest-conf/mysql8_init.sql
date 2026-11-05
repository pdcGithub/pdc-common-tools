#
# 说明：这里是关于数据库单元测试用的初始化脚本。在完成数据库初始化前，database 模块是没法测试的。
# 作者：庞东灿 ( Pang Dong Can )
# 创建日期：2026年10月20日
# 

# ======================== 1、使用 root 用户 登录数据库。然后创建 测试用的数据库 和 数据库用户

# root 用户创建 testdb 数据库
create database if not exists unittestdb character set = 'utf8mb4' ;
# root 用户创建用户(使用 % 来确保远程可连接)
create user if not exists 'unittest'@'%' identified by '1234567890-=aa';
# 数据库授权(把 unittestdb 中的所有权限都给到 unittest 用户。基础对象、存储过程、函数、事件、触发器)
grant all on unittestdb.* to 'unittest'@'%';
grant create routine on unittestdb.* to 'unittest'@'%';
grant event on unittestdb.* to 'unittest'@'%';
grant trigger on unittestdb.* to 'unittest'@'%';
# 刷新权限
flush privileges;

# 查看信息 （人工检查 数据库、用户、权限）
show databases;
select * from mysql.user;
show grants for 'unittest'@'%';

# ======================== 2、使用 unittest 用户 登录数据库。然后创建测试用的表、视图、函数、触发器

drop table if exists I_AM_MYSQL;
create table I_AM_MYSQL(
test_id int not null primary key comment '这是ID，测试用的',
test_name varchar(100) null comment '这是测试用的字符串'
) comment '这是测试用的表，用来验证数据库连接对不对';

select * from i_am_mysql;

# 插入一条测试数据
insert into i_am_mysql(test_id, test_name) values (1, 'first');

# 创建一个表，用于查询测试
drop table if exists student;
create table student(
stu_no varchar(100) not null primary key comment '学生编码',
stu_name varchar(200) comment '学生名称',
birth_date date comment '出生日期',
enroll_date date comment '入学日期',
gender varchar(1) comment '性别 M 男, F 女',
address varchar(800) comment '家庭住址',
tel varchar(200) comment '联系电话',
create_user varchar(100) not null comment '创建人',
create_time datetime(6) not null comment '创建时间'
) comment '这个表用于进行 框架的查询测试。这里的一些数据类型，应该和其它数据库通用，或者可以平替。';

insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000001', '张三', '1976-10-1', '1983-7-1', 'M', '英国白金汉宫', '001-002-003-004', 'ADMIN00001', now(6));
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000002', '李四', '1978-5-2', '1985-7-1', 'F', '美国三藩市', '35353535353', 'ADMIN00001', now(6));
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000003', '王五', '1976-8-3', '1983-7-1', 'M', '法国巴黎', '54554545544', 'ADMIN00001', now(6));
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000004', '赵六', '1977-3-4', '1984-7-1', 'F', '德国柏林', '45544554', 'ADMIN00001', now(6));
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000005', 'Harry Poter', '1979-6-2', '1987-7-1', 'M', '英国伦敦', '534353534543', 'ADMIN00001', now(6));

# 查询下结果
select * from student ;

# 创建一个视图 用于测试。视图上有一些统计处理
create or replace view v_student
as
select stu_no, stu_name, 
       birth_date, timestampdiff(YEAR, birth_date, curdate()) as age,
       enroll_date, timestampdiff(YEAR, enroll_date, curdate()) as grade,
       if(gender='M', '男', '女') as gender,
       address, tel, create_user, create_time
  from student ;
  
# 查询下 视图结果
select * from v_student;

# 这里再构建一个 sql 语句，用于生成虚拟实体
select stu_no, stu_name, 
       timestampdiff(YEAR, birth_date, curdate()) as age, 
       timestampdiff(YEAR, enroll_date, curdate()) as grade, 
       substr(address, 1, 2) country
  from student;
