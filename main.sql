create table applications (
  ip varchar(16) not null primary key,
  apps longtext not null default '',
  compname varchar(200) not null default 'pc'
) default charset=utf8;