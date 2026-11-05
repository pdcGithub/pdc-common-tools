/**
 * 说明：这里是关于数据库单元测试用的初始化脚本。在完成数据库初始化前，database 模块是没法测试的。
 * 作者：庞东灿 ( Pang Dong Can )
 * 创建日期：2026年10月21日
**/

/* ======================== 1、使用 sa 用户 登录数据库。然后创建 测试用的数据库 和 数据库用户
                               在 SQL Server 2016 中，SQL Server 的安全模型把“连接服务器”和“访问数据库”这两件事分开了。
							   CREATE LOGIN 创建的是服务器级别的登录名，用来通过身份验证连接到 SQL Server 实例；
							   CREATE USER 创建的是数据库级别的用户，用来在特定数据库中拥有权限、访问对象。 */

/* 切换到系统数据库 master */
use master;
go

/* 创建 单元测试 用的 数据库 unittestdb */
create database unittestdb;
go

/* 创建用户并指定默认的数据库，授权 db_owner */
create login unittest with password = '1234567890-=aa', default_database = unittestdb;
go

use unittestdb;
go

create user unittest for login unittest with default_schema = dbo;
go

alter role db_owner add member unittest;
go

/* ======================== 2、使用 unittest 用户 登录数据库。然后创建测试用的表、视图、函数、触发器 */

drop table if exists i_am_sqlserver;;
create table i_am_sqlserver(
test_id int not null primary key,
test_name varchar(100)
);
-- 通过维护元数据
-- 给表添加注释
EXEC sp_addextendedproperty @name = N'MS_Description', @value = '这是测试用的表，用来验证数据库连接对不对', @level0type = N'SCHEMA', @level0name = 'dbo', @level1type = N'TABLE', @level1name = 'i_am_sqlserver', @level2type = null, @level2name = null;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = '这是ID，测试用的', @level0type = N'SCHEMA', @level0name = 'dbo', @level1type = N'TABLE', @level1name = 'i_am_sqlserver', @level2type = N'COLUMN', @level2name = 'test_id';
EXEC sp_addextendedproperty @name = N'MS_Description', @value = '这是测试用的字符串', @level0type = N'SCHEMA', @level0name = 'dbo', @level1type = N'TABLE', @level1name = 'i_am_sqlserver', @level2type = N'COLUMN', @level2name = 'test_name';
go

-- 修改注释可使用 sp_updateextendedproperty
-- 删除注释可使用 sp_dropextendedproperty

-- 插入一条测试数据
insert into i_am_sqlserver(test_id, test_name) values(1, 'first');
go

-- 查询元数据中的注释信息
SELECT c.name AS ColumnName, ep.value AS ColumnDescription 
  FROM sys.columns c 
  LEFT JOIN sys.extended_properties ep ON ep.major_id = c.object_id AND ep.minor_id = c.column_id
WHERE c.object_id = OBJECT_ID('i_am_sqlserver');
-- 查询表注释
select * from sys.extended_properties e where e.major_id=OBJECT_ID('i_am_sqlserver') and e.minor_id=0;

select * from sys.extended_properties;

-- 查询数据
select * from i_am_sqlserver;

/* ======================== 创建一个表，用于查询测试 */
drop table if exists student;
create table student(
stu_no varchar(100) not null primary key ,
stu_name varchar(200) ,
birth_date date ,
enroll_date date ,
gender varchar(1) ,
address varchar(800) ,
tel varchar(200) ,
create_user varchar(100) not null ,
create_time datetime2(6) not null /* datetime2 精度最大是 7 */
);
-- 通过维护元数据
-- 给表添加注释
EXEC sp_addextendedproperty @name = N'MS_Description', @value = '这个表用于进行 框架的查询测试。这里的一些数据类型，应该和其它数据库通用，或者可以平替。', @level0type = N'SCHEMA', @level0name = 'dbo', @level1type = N'TABLE', @level1name = 'student', @level2type = null, @level2name = null;
EXEC sp_addextendedproperty @name = N'MS_Description', @value = '学生编码', @level0type = N'SCHEMA', @level0name = 'dbo', @level1type = N'TABLE', @level1name = 'student', @level2type = N'COLUMN', @level2name = 'stu_no';
EXEC sp_addextendedproperty @name = N'MS_Description', @value = '学生名称', @level0type = N'SCHEMA', @level0name = 'dbo', @level1type = N'TABLE', @level1name = 'student', @level2type = N'COLUMN', @level2name = 'stu_name';
EXEC sp_addextendedproperty @name = N'MS_Description', @value = '出生日期', @level0type = N'SCHEMA', @level0name = 'dbo', @level1type = N'TABLE', @level1name = 'student', @level2type = N'COLUMN', @level2name = 'birth_date';
EXEC sp_addextendedproperty @name = N'MS_Description', @value = '入学日期', @level0type = N'SCHEMA', @level0name = 'dbo', @level1type = N'TABLE', @level1name = 'student', @level2type = N'COLUMN', @level2name = 'enroll_date';
EXEC sp_addextendedproperty @name = N'MS_Description', @value = '性别 M 男, F 女', @level0type = N'SCHEMA', @level0name = 'dbo', @level1type = N'TABLE', @level1name = 'student', @level2type = N'COLUMN', @level2name = 'gender';
EXEC sp_addextendedproperty @name = N'MS_Description', @value = '家庭住址', @level0type = N'SCHEMA', @level0name = 'dbo', @level1type = N'TABLE', @level1name = 'student', @level2type = N'COLUMN', @level2name = 'address';
EXEC sp_addextendedproperty @name = N'MS_Description', @value = '联系电话', @level0type = N'SCHEMA', @level0name = 'dbo', @level1type = N'TABLE', @level1name = 'student', @level2type = N'COLUMN', @level2name = 'tel';
EXEC sp_addextendedproperty @name = N'MS_Description', @value = '创建人', @level0type = N'SCHEMA', @level0name = 'dbo', @level1type = N'TABLE', @level1name = 'student', @level2type = N'COLUMN', @level2name = 'create_user';
EXEC sp_addextendedproperty @name = N'MS_Description', @value = '创建时间', @level0type = N'SCHEMA', @level0name = 'dbo', @level1type = N'TABLE', @level1name = 'student', @level2type = N'COLUMN', @level2name = 'create_time';
go

-- 添加测试数据
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000001', '张三', '1976-10-1', '1983-7-1', 'M', '英国白金汉宫', '001-002-003-004', 'ADMIN00001', SYSDATETIME());
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000002', '李四', '1978-5-2', '1985-7-1', 'F', '美国三藩市', '35353535353', 'ADMIN00001', SYSDATETIME());
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000003', '王五', '1976-8-3', '1983-7-1', 'M', '法国巴黎', '54554545544', 'ADMIN00001', SYSDATETIME());
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000004', '赵六', '1977-3-4', '1984-7-1', 'F', '德国柏林', '45544554', 'ADMIN00001', SYSDATETIME());
insert into student(stu_no, stu_name, birth_date, enroll_date, gender, address, tel, create_user, create_time) 
values('S0000005', 'Harry Poter', '1979-6-2', '1987-7-1', 'M', '英国伦敦', '534353534543', 'ADMIN00001', SYSDATETIME());

-- 查询下结果
select * from student ;

-- 创建一个视图 用于测试。视图上有一些统计处理
create view v_student
as 
select stu_no, stu_name, 
       birth_date, datediff(YEAR, birth_date, GETDATE()) as age,
       enroll_date, datediff(YEAR, enroll_date, GETDATE()) as grade,
       case when gender='M' then '男' else '女' end as gender,
       address, tel, create_user, create_time
  from student ;

-- 查询视图
select * from v_student;

-- 这里再构建一个 sql 语句，用于生成虚拟实体
select stu_no, stu_name, 
       datediff(YEAR, birth_date, GETDATE()) as age, 
       datediff(YEAR, enroll_date, GETDATE()) as grade, 
       substring(address, 1, 2) country
  from student;