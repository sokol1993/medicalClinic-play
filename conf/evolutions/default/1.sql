# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table doctor (
  id                            bigserial not null,
  first_name                    varchar(255),
  last_name                     varchar(255),
  specialization_id             bigint,
  monday_hours                  varchar(255),
  tuesday_hours                 varchar(255),
  wednesday_hours               varchar(255),
  thursday_hours                varchar(255),
  friday_hours                  varchar(255),
  constraint pk_doctor primary key (id)
);

create table medical_visit (
  id                            bigserial not null,
  patient_id                    bigint,
  doctor_id                     bigint,
  date                          date,
  time                          time,
  is_completed                  boolean,
  constraint pk_medical_visit primary key (id)
);

create table medical_visit_list (
  id                            bigserial not null,
  date                          date,
  time                          time,
  doctor_id                     bigint,
  constraint pk_medical_visit_list primary key (id)
);

create table patient (
  id                            bigserial not null,
  first_name                    varchar(255),
  last_name                     varchar(255),
  email                         varchar(255),
  date_of_birth                 date,
  pesel                         varchar(255),
  street_name                   varchar(255),
  street_number                 varchar(255),
  home_number                   varchar(255),
  postcode                      varchar(255),
  city                          varchar(255),
  constraint pk_patient primary key (id)
);

create table security_role (
  id                            bigserial not null,
  name                          varchar(255),
  constraint pk_security_role primary key (id)
);

create table specialization (
  id                            bigserial not null,
  specialization_name           varchar(255),
  constraint pk_specialization primary key (id)
);

create table token (
  id                            bigserial not null,
  token                         varchar(255),
  constraint pk_token primary key (id)
);

create table users (
  id                            bigserial not null,
  username                      varchar(255),
  password                      varchar(255),
  active                        boolean,
  patient_id                    bigint,
  doctor_id                     bigint,
  token_id                      bigint,
  constraint uq_users_patient_id unique (patient_id),
  constraint uq_users_doctor_id unique (doctor_id),
  constraint uq_users_token_id unique (token_id),
  constraint pk_users primary key (id)
);

create table users_security_role (
  users_id                      bigint not null,
  security_role_id              bigint not null,
  constraint pk_users_security_role primary key (users_id,security_role_id)
);

create table users_user_permission (
  users_id                      bigint not null,
  user_permission_id            bigint not null,
  constraint pk_users_user_permission primary key (users_id,user_permission_id)
);

create table user_permission (
  id                            bigserial not null,
  permission_value              varchar(255),
  constraint pk_user_permission primary key (id)
);

alter table doctor add constraint fk_doctor_specialization_id foreign key (specialization_id) references specialization (id) on delete restrict on update restrict;
create index ix_doctor_specialization_id on doctor (specialization_id);

alter table medical_visit add constraint fk_medical_visit_patient_id foreign key (patient_id) references patient (id) on delete restrict on update restrict;
create index ix_medical_visit_patient_id on medical_visit (patient_id);

alter table medical_visit add constraint fk_medical_visit_doctor_id foreign key (doctor_id) references doctor (id) on delete restrict on update restrict;
create index ix_medical_visit_doctor_id on medical_visit (doctor_id);

alter table medical_visit_list add constraint fk_medical_visit_list_doctor_id foreign key (doctor_id) references doctor (id) on delete restrict on update restrict;
create index ix_medical_visit_list_doctor_id on medical_visit_list (doctor_id);

alter table users add constraint fk_users_patient_id foreign key (patient_id) references patient (id) on delete restrict on update restrict;

alter table users add constraint fk_users_doctor_id foreign key (doctor_id) references doctor (id) on delete restrict on update restrict;

alter table users add constraint fk_users_token_id foreign key (token_id) references token (id) on delete restrict on update restrict;

alter table users_security_role add constraint fk_users_security_role_users foreign key (users_id) references users (id) on delete restrict on update restrict;
create index ix_users_security_role_users on users_security_role (users_id);

alter table users_security_role add constraint fk_users_security_role_security_role foreign key (security_role_id) references security_role (id) on delete restrict on update restrict;
create index ix_users_security_role_security_role on users_security_role (security_role_id);

alter table users_user_permission add constraint fk_users_user_permission_users foreign key (users_id) references users (id) on delete restrict on update restrict;
create index ix_users_user_permission_users on users_user_permission (users_id);

alter table users_user_permission add constraint fk_users_user_permission_user_permission foreign key (user_permission_id) references user_permission (id) on delete restrict on update restrict;
create index ix_users_user_permission_user_permission on users_user_permission (user_permission_id);


# --- !Downs

alter table if exists doctor drop constraint if exists fk_doctor_specialization_id;
drop index if exists ix_doctor_specialization_id;

alter table if exists medical_visit drop constraint if exists fk_medical_visit_patient_id;
drop index if exists ix_medical_visit_patient_id;

alter table if exists medical_visit drop constraint if exists fk_medical_visit_doctor_id;
drop index if exists ix_medical_visit_doctor_id;

alter table if exists medical_visit_list drop constraint if exists fk_medical_visit_list_doctor_id;
drop index if exists ix_medical_visit_list_doctor_id;

alter table if exists users drop constraint if exists fk_users_patient_id;

alter table if exists users drop constraint if exists fk_users_doctor_id;

alter table if exists users drop constraint if exists fk_users_token_id;

alter table if exists users_security_role drop constraint if exists fk_users_security_role_users;
drop index if exists ix_users_security_role_users;

alter table if exists users_security_role drop constraint if exists fk_users_security_role_security_role;
drop index if exists ix_users_security_role_security_role;

alter table if exists users_user_permission drop constraint if exists fk_users_user_permission_users;
drop index if exists ix_users_user_permission_users;

alter table if exists users_user_permission drop constraint if exists fk_users_user_permission_user_permission;
drop index if exists ix_users_user_permission_user_permission;

drop table if exists doctor cascade;

drop table if exists medical_visit cascade;

drop table if exists medical_visit_list cascade;

drop table if exists patient cascade;

drop table if exists security_role cascade;

drop table if exists specialization cascade;

drop table if exists token cascade;

drop table if exists users cascade;

drop table if exists users_security_role cascade;

drop table if exists users_user_permission cascade;

drop table if exists user_permission cascade;

