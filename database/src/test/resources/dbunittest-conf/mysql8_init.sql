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


