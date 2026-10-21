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

