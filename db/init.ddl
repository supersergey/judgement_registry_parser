create table cause_categories
(
  category_code int not null
    primary key,
  name text null
)
  engine=InnoDB
;

create table courts
(
  court_code int not null
    primary key,
  name text null,
  instance_code int null,
  region_code int null
)
  engine=InnoDB
;

create index courts_instances_instance_code_fk
  on courts (instance_code)
;

create index courts_regions_region_code_fk
  on courts (region_code)
;

create table documents
(
  doc_id int not null
    primary key,
  court_code int null,
  judgment_code int null,
  justice_kind int null,
  category_code text null,
  cause_num varchar(64) null,
  adjudication_date text null,
  receipt_date text null,
  judge text null,
  doc_url text null,
  status int null,
  date_publ text null,
  constraint documents_courts_court_code_fk
  foreign key (court_code) references courts (court_code)
)
  engine=InnoDB
;

create index documents_courts_court_code_fk
  on documents (court_code)
;

create index documents_cause_num_index
  on documents (cause_num)
;

create table instances
(
  instance_code int not null
    primary key,
  name text null
)
  engine=InnoDB
;

alter table courts
  add constraint courts_instances_instance_code_fk
foreign key (instance_code) references instances (instance_code)
;

create table judgment_forms
(
  judgment_code int not null
    primary key,
  name text null
)
  engine=InnoDB
;

create table justice_kinds
(
  justice_kind int not null
    primary key,
  name text null
)
  engine=InnoDB
;

create table regions
(
  region_code int not null
    primary key,
  name text null
)
  engine=InnoDB
;

alter table courts
  add constraint courts_regions_region_code_fk
foreign key (region_code) references regions (region_code)
;

