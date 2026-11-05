/**
 * 说明：这里是关于数据库单元测试用的初始化脚本。在完成数据库初始化前，database 模块是没法测试的。
 * 作者：庞东灿 ( Pang Dong Can )
 * 创建日期：2026年10月21日
**/

/* ======================== 1、使用 SYS 或者 SYSTEM 用户 登录数据库。然后创建 测试用的数据库 和 数据库用户
                               在oracle中，数据库用户需要指定 数据表空间 和 临时表空间。因为这里是测试，这里就使用默认值 数据表空间是 USER，临时表空间是 TEMP */

/* 创建用户并指定表空间 */
create user unittest identified by "1234567890-=aa" default tablespace USERS temporary tablespace TEMP;
 
/* 给用户授予权限，让用户可以满足日常使用，但是不允许授予 DBA 角色。因为 DBA 权限过大 */
grant connect,resource to unittest;
grant create public database link to unittest ;
grant create view to unittest ;
grant create procedure to unittest ;
grant select any dictionary to unittest ;
grant unlimited tablespace to unittest ;
grant create materialized view to unittest;
grant debug any procedure, debug connect session to unittest;

/* ======================== 2、使用 unittest 用户 登录数据库。然后创建测试用的表、视图、函数、触发器 */
drop table I_AM_ORACLE;
create table I_AM_ORACLE(
test_id int not null primary key,
test_name varchar2(100) null
);
comment on table I_AM_ORACLE is '这是测试用的表，看看连接的数据对不对';
comment on column I_AM_ORACLE.test_id is '这是测试用的主键';
comment on column I_AM_ORACLE.test_name is '这是测试用的名称';

-- 查询数据
select * from i_am_oracle ;

-- 插入一条测试数据
insert into i_am_oracle(test_id, test_name) values (1, 'first');
commit;

/* ======================== 创建一个表 student，用于查询测试 */

drop table student;
create table student(
stu_no varchar2(100) not null primary key ,
stu_name varchar2(200) ,
birth_date date ,
enroll_date date ,
gender varchar2(1) ,
address varchar2(800) ,
tel varchar(200) ,
create_user varchar(100) not null ,
create_time timestamp(6) not null
);
comment on table student is '这个表用于进行 框架的查询测试。这里的一些数据类型，应该和其它数据库通用，或者可以平替。';
comment on column student.stu_no is '学生编码';
comment on column student.stu_name is '学生名称';
comment on column student.birth_date is '出生日期';
comment on column student.enroll_date is '入学日期';
comment on column student.gender is '性别 M 男, F 女';
comment on column student.address is '家庭住址';
comment on column student.tel is '联系电话';
comment on column student.create_user is '创建人';
comment on column student.create_time is '创建时间';

-- 清空数据，重新插入
-- delete from student ;
-- commit;

-- 插入测试数据
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000001', '张三', date '1976-10-1', date '1983-7-1', 'M', '英国白金汉宫', '001-002-003-004', 'ADMIN00001', systimestamp);
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000002', '李四', date '1978-5-2', date '1985-7-1', 'F', '美国三藩市', '35353535353', 'ADMIN00001', systimestamp);
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000003', '王五', date '1976-8-3', date '1983-7-1', 'M', '法国巴黎', '54554545544', 'ADMIN00001', systimestamp);
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000004', '赵六', date '1977-3-4', date '1984-7-1', 'F', '德国柏林', '45544554', 'ADMIN00001', systimestamp);
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000005', 'Harry Poter', date '1979-6-2', date '1987-7-1', 'M', '英国伦敦', '534353534543', 'ADMIN00001', systimestamp);
commit;

-- 查询结果
select * from student;

-- 创建一个视图 用于测试。视图上有一些统计处理
create or replace view v_student
as
select stu_no, stu_name, 
       birth_date, trunc(months_between(sysdate, birth_date)/12) as age,
       enroll_date, trunc(months_between(sysdate, enroll_date)/12) as grade,
       case when gender='M' then '男' else '女' end as gender,
       address, tel, create_user, create_time
  from student ;

-- 查询
select * from v_student;

-- 这里再构建一个 sql 语句，用于生成虚拟实体
select stu_no, stu_name, 
       trunc(months_between(sysdate, birth_date)/12) as age, 
       trunc(months_between(sysdate, enroll_date)/12) as grade, 
       substr(address, 1, 2) country
  from student;
