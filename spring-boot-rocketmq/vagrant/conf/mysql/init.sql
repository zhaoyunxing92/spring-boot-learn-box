-- 创建cherry数据库
create database if not exists cherry character set utf8 collate utf8_general_ci;
-- 使用cherry数据库
-- use cherry;

-- 账户
create table if not exists cherry.cherry_account (
	id bigint not null auto_increment comment '主键id',
	user_name varchar(32) default null comment '账户名称',
	email varchar(32) default null comment '账户邮箱',
	password varchar(128) not null comment '账户密码',
	nickname varchar(16) default null comment '昵称',
	real_name varchar(16) default null comment '昵称',
	intro varchar(256) null comment '介绍',
	avatar varchar(256) null comment 'logo地址',
	account_non_expired tinyint(1) unsigned default 1 not null comment '账户是否过期：0=过期，1=未过期',
	account_non_locked tinyint(1) unsigned default 1 not null comment '账户是否锁定：0=锁定，1=未锁定',
	enabled tinyint(1) unsigned default 1 not null comment '是否启用：0=不启用，1=启用',
	creator bigint not null comment '创建人',
	creator_time timestamp default current_timestamp not null comment '创建时间',
	modifier bigint not null comment '修改人',
	modifier_time timestamp default current_timestamp on update current_timestamp not null comment '修改时间',
	primary key (id),
	unique key uk_name (user_name),
	unique key uk_email (email)
) engine=innodb default charset=utf8 comment='账户表' auto_increment=1000 ;

-- 角色
create table if not exists cherry.cherry_role (
	id bigint not null auto_increment comment '主键id',
	name varchar(32) not null comment '角色名称',
	intro varchar(64) null comment '权限简介',
	status tinyint unsigned default 1 not null comment '0=删除，1=正常，2=冻结',
	creator bigint not null comment '创建人',
	creator_time timestamp default current_timestamp not null comment '创建时间',
	modifier bigint not null comment '修改人',
	modifier_time timestamp default current_timestamp on update current_timestamp not null comment '修改时间',
	primary key (id),
	unique key uk_name (name)
) engine=innodb default charset=utf8 comment='角色表' auto_increment=1000 ;

-- 权限
create table if not exists cherry.cherry_permissions (
	id bigint not null auto_increment comment '主键id',
	name varchar(32) not null comment '权限名称',
	intro varchar(64) null comment '权限简介',
	sys tinyint(1) unsigned default 0 not null comment '0=普通权限，1=系统权限',
	status tinyint unsigned default 1 not null comment '0=删除，1=正常，2=冻结',
	creator bigint not null comment '创建人',
	creator_time timestamp default current_timestamp not null comment '创建时间',
	modifier bigint not null comment '修改人',
	modifier_time timestamp default current_timestamp on update current_timestamp not null comment '修改时间',
	primary key (id),
	unique key uk_name (name)
) engine=innodb default charset=utf8 comment='权限表' auto_increment=1000 ;

-- 角色权限
create table if not exists cherry.cherry_role_permissions (
	role_id bigint not null comment '角色id',
	permissions_id bigint not null comment '权限id',
	creator_time timestamp default current_timestamp not null comment '创建时间',
	modifier_time timestamp default current_timestamp on update current_timestamp not null comment '修改时间',
	unique key uk_role_permissions (role_id,permissions_id)
) engine=innodb default charset=utf8 comment='角色权限表';

-- 账户角色
create table if not exists cherry.cherry_account_role (
    account_id bigint not null comment '账户id',
	role_id bigint not null comment '角色id',
	creator_time timestamp default current_timestamp not null comment '创建时间',
	modifier_time timestamp default current_timestamp on update current_timestamp not null comment '修改时间',
	unique key uk_account_role (account_id,role_id)
) engine=innodb default charset=utf8 comment='账户角色表';

-- 团队表
create table if not exists cherry.cherry_team (
	id bigint not null auto_increment comment '主键id',
	name varchar(32) not null comment '团队名称',
	intro varchar(256) null comment '团队介绍',
	avatar varchar(256) null comment '团队logo地址',
	status tinyint unsigned default 1 not null comment '0=删除，1=正常，2=冻结',
	limit_member int unsigned default 10 not null comment '团队最多容纳人员',
	creator bigint not null comment '创建人',
	creator_time timestamp default current_timestamp not null comment '创建时间',
	modifier bigint not null comment '修改人',
	modifier_time timestamp default current_timestamp on update current_timestamp not null comment '修改时间',
	primary key (id)
) engine=innodb default charset=utf8 comment='团队表' auto_increment=1000 ;

-- 团队成员表
create table if not exists cherry.cherry_team_member (
    team_id bigint not null  comment '团队id',
	user_id bigint not null  comment '成员id',
	nickname varchar(16) not null comment '昵称',
	status tinyint unsigned default 1 not null comment '0=删除，1=正常，2=冻结',
	manage tinyint(1) unsigned default 0 not null comment '0=成员，1=管理员',
	creator bigint not null comment '创建人',
	creator_time timestamp default current_timestamp not null comment '创建时间',
	modifier bigint not null comment '修改人',
	modifier_time timestamp default current_timestamp on update current_timestamp not null comment '修改时间',
	CONSTRAINT uk_team_user UNIQUE KEY (team_id,user_id)
) engine=innodb default charset=utf8 comment='团队成员表';

-- 事物日志表
CREATE TABLE if not exists cherry.tx_msg (
	id varchar(64) NOT NULL COMMENT '事物消息id',
	`action` varchar(100) NOT NULL COMMENT '表示当前执行的动作',
	`modifier_time` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL on update CURRENT_TIMESTAMP COMMENT '插入时间',
	 PRIMARY KEY tx_msg_pk (id)
)ENGINE=InnoDB COMMENT='事物消息记录表' ;

-- 插入账户
insert ignore into cherry.cherry_account
(user_name, email, password, intro, creator, modifier)
values('zhaoyunxing', '2385585770@qq.com', '$2a$10$AJHaPBbIf0qldHwfdOt1k.I5tvYGFsVboYIQMTl357IVbHjtCzNIu',  'java程序员', 0, 0),('sunny', 'zhaoyunxing@163.com', '$2a$10$j4EgfxhiqqiBeXU8NTOaaeYYCwOC6fpmO7eGJoBQael30Nz4b45K2',  'java程序员', 0, 0);


--  插入角色
insert ignore into cherry.cherry_role
(name, intro,creator,modifier)
values('admin', '超级管理',0,0),('normal_user', '普通用户',0,0);

-- 插入系统权限
INSERT INTO cherry.cherry_permissions
(name, intro, sys, creator, modifier)
VALUES('delete_account', '删除账户权限', 1, 0, 0),('update_account', '更新账户权限', 1, 0, 0),('add_account', '添加账户权限', 1, 0, 0);


--  插入角色权限
insert ignore into cherry.cherry_role_permissions
(role_id, permissions_id)
values(1000, 1000),(1000, 1001),(1000, 1003),(1001, 1001);

-- 账户授权
insert ignore into cherry.cherry_account_role
(account_id, role_id)
values(1000, 1000),(1001, 1001);
