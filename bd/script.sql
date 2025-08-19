create database gamecore;
use gamecore;

create table empresa(
	id int primary key auto_increment,
    nome varchar(75) not null,
    cnpj char(14),
    telefone char(14),
    
    unique ix_cnpj (cnpj)
);

create table token(
	fk_empresa int not null primary key,
	codigo varchar(25),
	foreign key fk_empresa (fk_empresa) references empresa(id),
	unique ix_codigo (codigo)
);

create table usuario(
	id int primary key auto_increment,
	nome varchar(50),
	email varchar(150),
	cpf char(11),
	senha varchar(255),
	fk_empresa int not null,
	foreign key fk_empresa (fk_empresa) references empresa(id),
	unique ix_email (email),
	unique ix_cpf (cpf)
);

create table servidor(
	id int primary key auto_increment,
    fk_empresa int not null,
    localizacao varchar(45) not null,

	foreign key fk_empresa (fk_empresa) references empresa(id)
);

create table alerta(
	id int primary key auto_increment,
    minimo decimal(5, 2) not null,
    maximo decimal(5, 2) not null,
    componente varchar(45) not null,
    
    fk_servidor int not null,
    
    foreign key fk_servidor (fk_servidor) references servidor(id)
);