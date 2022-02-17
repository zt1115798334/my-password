drop schema if exists `my-password`;
create schema `my-password`;
use `my-password`;

drop table if exists t_user;
create table t_user
(
    id              bigint auto_increment
        primary key,
    account         varchar(20)                                           not null comment '账户',
    password        varchar(255)                                          not null comment '密码',
    salt            varchar(255)                                          not null comment '盐',
    user_name       varchar(20)                                           null comment '用户名',
    phone           varchar(100)                                          null comment '手机号',
    account_type    enum ('ADMIN','ORDINARY')                             not null comment '账户类型',
    enabled_state   enum ('OFF','ON')           default 'ON'              not null comment '开启状态 ',
    created_time    datetime                    default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_time    datetime                                              null comment '更新时间',
    last_login_time datetime                                              null comment '最后登录时间',
    delete_state    enum ('UN_DELETE','DELETE') default 'UN_DELETE'       not null comment '删除状态'
)
    comment '用户表' collate = utf8_unicode_ci;

INSERT INTO t_user (id, account, password, salt, user_name, phone, account_type,
                    enabled_state, created_time, updated_time, last_login_time, delete_state)
VALUES (1, '12911855304', 'f9f81c58749ca8d68afc209d6c37e5236ac038ce', '7bb244443f7e9e85', 'test', null, 'ADMIN',
        'ON', '2021-09-13 17:32:25', null, null, 'UN_DELETE');

drop table if exists t_user_log;
create table t_user_log
(
    id       bigint auto_increment
        primary key,
    user_id  bigint                             null comment '操作员ID',
    name     varchar(255)                       null comment '操作员姓名',
    type     varchar(255)                       null comment '操作类型',
    content  text                               null comment '操作详情',
    ip       varchar(20)                        null comment 'IP',
    time     datetime default CURRENT_TIMESTAMP null,
    classify varchar(255)                       null comment '类',
    fun      varchar(255)                       null comment '方法',
    response text                               null comment '返回值'
)
    comment '用户日志表' collate = utf8_unicode_ci;

create fulltext index ft_type
    on t_user_log (type, ip, content, name, classify, fun);

create index index_name
    on t_user_log (user_id);

drop table if exists t_verification_code_log;
create table t_verification_code_log
(
    id                     bigint auto_increment
        primary key,
    notice_content         varchar(200)                       not null comment '通知内容',
    verification_code      char(10)                           not null comment ' 验证码',
    verification_code_type enum ('REG','LOGIN')               not null comment '验证码类型',
    ip                     varchar(40)                        not null comment 'ip地址',
    created_time           datetime default CURRENT_TIMESTAMP not null comment '创建时间'
) comment '验证码日志表' collate = utf8_unicode_ci;

drop table if exists t_permission;
create table t_permission
(
    id                  bigint auto_increment
        primary key,
    permission_name     varchar(20)                        not null comment '权限名称',
    permission_nickname varchar(20)                        not null comment '权限昵称',
    permission          varchar(40)                        not null comment '权限字符串',
    permission_type     enum ('DISPLAY','OPERATION')       not null comment '权限类型',
    url                 varchar(255)                       not null comment 'url地址',
    path                varchar(50)                        null comment '前台地址',
    icon                varchar(50)                        null comment '图标',
    order_by            int                                not null comment '排序',
    created_time        datetime default CURRENT_TIMESTAMP not null comment '创建时间'
) comment '权限表' collate = utf8_unicode_ci;

insert into t_permission (permission_name, permission_nickname, permission, permission_Type, url, path, icon, order_by,
                          created_time)
values ('首页-显示', '首页', 'index:show', 'DISPLAY', '/api/index/**', '/home', 'el-icon-copy-document', 1, now()),
       ('文件库-显示', '文件库', 'library:show', 'DISPLAY', '/api/library/**', '/library', 'el-icon-document-remove', 1, now()),
       ('素材库-显示', '素材库', 'material:show', 'DISPLAY', '/api/material/**', '/material', 'el-icon-folder', 1, now()),
       ('作者库-显示', '作者库', 'author:show', 'DISPLAY', '/api/author/**', '/authorManage', 'el-icon-user-solid', 1, now()),
       ('图片库-显示', '图片库', 'gallery:show', 'DISPLAY', '/api/gallery/**', '/pictureManage', 'el-icon-menu', 1, now()),
       ('文件库-上传', '显示', 'user:delete', 'OPERATION', '/api/library/importLibrary', null, null, 1, now()),
       ('文件库-修改', '修改', 'user:delete', 'OPERATION', '/api/library/modifyLibrary', null, null, 1, now()),
       ('文件库-下载', '下载', 'user:delete', 'OPERATION', '/api/library/exportLibrary', null, null, 1, now()),
       ('文件库-删除', '删除', 'user:delete', 'OPERATION', '/api/library/deleteLibrary', null, null, 1, now());

drop table if exists t_tab;
create table t_tab
(
    id           bigint auto_increment primary key,
    user_id      bigint                                                not null comment '用户ID',
    name         varchar(50)                                           not null comment '名称',
    colour       varchar(50)                                           not null comment '颜色',
    info_class   enum ('SYSTEM','CUSTOM')                              not null comment '信息等级',
    created_time datetime                    default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_time datetime                                              null comment '更新时间',
    delete_state enum ('UN_DELETE','DELETE') default 'UN_DELETE'       not null comment '删除状态'
) comment '标签表' collate = utf8_unicode_ci;


drop table if exists t_profiles_picture;
create table t_profiles_picture
(
    id           bigint auto_increment primary key,
    user_id      bigint                                                not null comment '用户ID',
    path         varchar(500)                                          not null comment '路径',
    created_time datetime                    default CURRENT_TIMESTAMP not null comment '创建时间',
    delete_state enum ('UN_DELETE','DELETE') default 'UN_DELETE'       not null comment '删除状态'
) comment '头像表';

drop table if exists t_safe_deposit_box;
create table t_safe_deposit_box
(
    id            bigint auto_increment primary key,
    user_id       bigint                                                not null comment '用户ID',
    safe_name     varchar(50)                                           not null comment '安全名称',
    safe_account  varchar(50)                                           not null comment '安全账户',
    safe_password varchar(50)                                           not null comment '颜色',
    deposit_type  enum ('BANK_CARD','COMMON')                           not null comment '信息等级',
    created_time  datetime                    default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_time  datetime                                              null comment '更新时间',
    delete_state  enum ('UN_DELETE','DELETE') default 'UN_DELETE'       not null comment '删除状态'
) comment '保险箱表' collate = utf8_unicode_ci;