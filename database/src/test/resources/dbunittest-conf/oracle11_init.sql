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

