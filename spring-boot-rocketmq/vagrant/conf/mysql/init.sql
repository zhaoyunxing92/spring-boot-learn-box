-- 创建bank数据库
create database if not exists bank01 character set utf8 collate utf8_general_ci;
create database if not exists bank02 character set utf8 collate utf8_general_ci;

-- bank01
create table if not exists bank01.account (
	account_name varchar(32) default null comment '账户名称',
	money bigint not null default 1000 comment '账户金额',
	modifier_time timestamp default current_timestamp on update current_timestamp not null comment '修改时间',
	primary key uk_name (account_name)
) engine=innodb default charset=utf8 comment='账户表';


create table if not exists bank01.tx_msg_log (
	`msg_id` varchar(64) not null comment '消息id',
	`creator_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	primary key pk_msg_id (msg_id)
) engine=innodb default charset=utf8 comment='事物消息日志';

CREATE table if not exists bank01.undo_log
(
  `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `branch_id`     BIGINT(20)   NOT NULL,
  `xid`           VARCHAR(100) NOT NULL,
  `context`       VARCHAR(128) NOT NULL,
  `rollback_info` LONGBLOB     NOT NULL,
  `log_status`    INT(11)      NOT NULL,
  `log_created`   DATETIME     NOT NULL,
  `log_modified`  DATETIME     NOT NULL,
  `ext`           VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 comment='seata 回滚使用日志表';

-- bank02
create table if not exists bank02.account (
	account_name varchar(32) default null comment '账户名称',
	money bigint not null default 10 comment '账户金额',
	modifier_time timestamp default current_timestamp on update current_timestamp not null comment '修改时间',
	primary key uk_name (user_name)
) engine=innodb default charset=utf8 comment='账户表';

create table if not exists bank02.tx_msg_log (
	`msg_id` varchar(64) not null comment '消息id',
	`creator_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	primary key pk_msg_id (msg_id)
) engine=innodb default charset=utf8 comment='事物消息日志';

CREATE table if not exists bank02.undo_log
(
  `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `branch_id`     BIGINT(20)   NOT NULL,
  `xid`           VARCHAR(100) NOT NULL,
  `context`       VARCHAR(128) NOT NULL,
  `rollback_info` LONGBLOB     NOT NULL,
  `log_status`    INT(11)      NOT NULL,
  `log_created`   DATETIME     NOT NULL,
  `log_modified`  DATETIME     NOT NULL,
  `ext`           VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 comment='seata 回滚使用日志表';

INSERT INTO bank01.account
(account_name)
VALUES('zhangsan');

INSERT INTO bank02.account
(account_name)
VALUES('lisi');
