create database if not exists thuanthanhlanddb;

create table if not exists categories (
	id int  not null,
	name varchar(191) not null,
	title varchar(191),
	slug varchar(191),
	parent_id int,
	hot Tinyint(4) default 0,
	public tinyint(4) default 1,
	created_at timestamp,
	updated_at timestamp,
	primary key (id)
);

create table if not exists news (
	id int  not null,
	name varchar(191) not null,
	
	title varchar(191) not null,
	description text not null,
	content longtext not null,
	slug varchar(191) not null,
	category_id int not null,	
	hot Tinyint(4) default 0,
	public tinyint(4) default 1,
	created_at timestamp,
	updated_at timestamp,
	primary key (id),
	foreign key (category_id) references categories(id) on update cascade
);

create table if not exists contracts (
	id int  not null,
	name varchar(191) not null,
	email varchar(191) not null,
	title varchar(191),
	address varchar(191),
	phone varchar(191),
	content longtext not null,
	status tinyint(4) default 0,
	created_at timestamp,
	updated_at timestamp,
	primary key (id)
);

create table if not exists slides (
	id int not null,
	name varchar(191),
	title varchar(191),
	image varchar(191) not null,
	sort int  not null unique,
	public tinyint(4) default 1,
	created_at timestamp,
	update_at timestamp,
	primary key (id)
);

create table if not exists users (
	id int not null,
	name varchar(191) not null,
	email varchar(191) not null,
	role tinyint(1) default 0,
	address varchar(191),
	password varchar(191) not null,
	phone varchar(191) not null,
	avatar varchar(191),
	created_at timestamp,
	update_at timestamp,
	primary key (id)
);



