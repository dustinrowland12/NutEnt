create sequence role_seq start 1 increment 1;
create sequence users_seq start 1 increment 1;
create table lu_account_locked_reason_code (reason_code_id int4 not null, code varchar(255), reason varchar(255), primary key (reason_code_id));
create table role (role_id int4 not null, role_name varchar(255), primary key (role_id));
create table user_role (user_id int4 not null, role_id int4 not null, primary key (role_id, user_id));
create table users (user_id int4 not null, created_by varchar(255), created_date timestamp, last_modified_by varchar(255), last_modified_date timestamp, account_locked boolean not null, last_login_date timestamp, first_name varchar(255), last_name varchar(255), middle_name varchar(255), password varchar(255), password_update_date timestamp, salt varchar(255), unsuccessful_login_attempts int4, username varchar(255), account_locked_reason_code int4, primary key (user_id));
alter table if exists user_role add constraint FKa68196081fvovjhkek5m97n3y foreign key (role_id) references role;
alter table if exists user_role add constraint FKj345gk1bovqvfame88rcx7yyx foreign key (user_id) references users;
alter table if exists users add constraint FKdqhe3lu9ftuv7ikqmbbwjpp54 foreign key (account_locked_reason_code) references lu_account_locked_reason_code;