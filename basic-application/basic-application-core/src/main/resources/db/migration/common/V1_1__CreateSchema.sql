CREATE SCHEMA IF NOT EXISTS basic_application;

--
-- Base model
--
create sequence basic_application.Authority_id_seq start 1 increment 1;
create sequence basic_application.City_id_seq start 1 increment 1;
create sequence basic_application.DataUpgradeRecord_id_seq start 1 increment 1;
create sequence basic_application.HistoryDifference_id_seq start 1 increment 1;
create sequence basic_application.HistoryLog_id_seq start 1 increment 1;
create sequence basic_application.Parameter_id_seq start 1 increment 1;
create sequence basic_application.QueuedTaskHolder_id_seq start 1 increment 1;
create sequence basic_application.user__id_seq start 1 increment 1;
create sequence basic_application.UserGroup_id_seq start 1 increment 1;
create table basic_application.Authority (id int8 not null, name text, primary key (id));
create table basic_application.Authority_customPermissionNames (Authority_id int8 not null, customPermissionNames text);
create table basic_application.BasicUser (id int8 not null, primary key (id));
create table basic_application.City (id int8 not null, deleteable boolean not null, disableable boolean not null, editable boolean not null, enabled boolean not null, position int4 not null, label_en text, label_fr text, postalCode text not null, primary key (id));
create table basic_application.DataUpgradeRecord (id int8 not null, autoPerform boolean not null, done boolean not null, executionDate timestamp, name text not null, primary key (id));
create table basic_application.HistoryDifference (id int8 not null, after_label text, after_reference_id int8, after_reference_type varchar(255), after_serialized text, before_label text, before_reference_id int8, before_reference_type varchar(255), before_serialized text, eventType varchar(255) not null, path_key_label text, path_key_reference_id int8, path_key_reference_type varchar(255), path_key_serialized text, path_path text not null, parentDifference_id int8, parentLog_id int8, differences_ORDER int4, primary key (id));
create table basic_application.HistoryLog (id int8 not null, comment text, date timestamp not null, eventType varchar(255) not null, mainObject_label text, mainObject_reference_id int8, mainObject_reference_type varchar(255), mainObject_serialized text, object1_label text, object1_reference_id int8, object1_reference_type varchar(255), object1_serialized text, object2_label text, object2_reference_id int8, object2_reference_type varchar(255), object2_serialized text, object3_label text, object3_reference_id int8, object3_reference_type varchar(255), object3_serialized text, object4_label text, object4_reference_id int8, object4_reference_type varchar(255), object4_serialized text, subject_label text, subject_reference_id int8, subject_reference_type varchar(255), subject_serialized text, primary key (id));
create table basic_application.Parameter (id int8 not null, name text not null, stringValue text, primary key (id));
create table basic_application.QueuedTaskHolder (id int8 not null, creationDate timestamp not null, endDate timestamp, name text not null, queueId text, report text, result varchar(255), serializedTask text not null, stackTrace text, startDate timestamp, status varchar(255) not null, taskType text not null, triggeringDate timestamp, optLock int4, primary key (id));
create table basic_application.TechnicalUser (id int8 not null, primary key (id));
create table basic_application.user_ (id int8 not null, active boolean not null, creationDate timestamp not null, lastLoginDate timestamp, lastUpdateDate timestamp not null, locale varchar(255), passwordHash text, userName text not null, email text, faxNumber text, firstName text not null, gsmNumber text, lastName text not null, phoneNumber text, passwordInformation_lastUpdateDate timestamp, passwordRecoveryRequest_creationDate timestamp, passwordRecoveryRequest_initiator varchar(255), passwordRecoveryRequest_token text, passwordRecoveryRequest_type varchar(255), primary key (id));
create table basic_application.user__Authority (user__id int8 not null, authorities_id int8 not null, primary key (user__id, authorities_id));
create table basic_application.user__passwordInformation_history (user__id int8 not null, passwordInformation_history text, history_ORDER int4 not null, primary key (user__id, history_ORDER));
create table basic_application.user__UserGroup (persons_id int8 not null, groups_id int8 not null, primary key (persons_id, groups_id));
create table basic_application.UserGroup (id int8 not null, description text, locked boolean not null, name text, primary key (id));
create table basic_application.UserGroup_Authority (UserGroup_id int8 not null, authorities_id int8 not null, primary key (UserGroup_id, authorities_id));
alter table basic_application.City add constraint UKcici6ao6snb79g0i2ebsix408 unique (label_fr, postalCode);
alter table basic_application.DataUpgradeRecord add constraint UK_6q54k3x0axoc3n8ns55emwiev unique (name);
create index idx_HistoryDifference_parentLog on basic_application.HistoryDifference (parentLog_id);
create index idx_HistoryDifference_parentDifference on basic_application.HistoryDifference (parentDifference_id);
alter table basic_application.Parameter add constraint UK_k31gbcltpas6ux95qpk19o6q5 unique (name);
alter table basic_application.user_ add constraint UK_q1sdxrqyk0i3sw35q3m92sx3m unique (userName);
alter table basic_application.Authority_customPermissionNames add constraint FK6uqor94s0128ryjrrdw84f67x foreign key (Authority_id) references basic_application.Authority;
alter table basic_application.BasicUser add constraint FKhjnq1hls89k8qbs2hpap2i09j foreign key (id) references basic_application.user_;
alter table basic_application.HistoryDifference add constraint FKbs6nv5pjsfuj7na52g16pfbrw foreign key (parentDifference_id) references basic_application.HistoryDifference;
alter table basic_application.HistoryDifference add constraint FK77dlplmm50qgjbl1cqv4nrgoi foreign key (parentLog_id) references basic_application.HistoryLog;
alter table basic_application.TechnicalUser add constraint FKq1c424ixlnmkb5c8w1bdddybj foreign key (id) references basic_application.user_;
alter table basic_application.user__Authority add constraint FKhoyhws618rm4v2rjvm0x9uuhk foreign key (authorities_id) references basic_application.Authority;
alter table basic_application.user__Authority add constraint FK1nrpbko4mct39gq143ef5jwq0 foreign key (user__id) references basic_application.user_;
alter table basic_application.user__passwordInformation_history add constraint FKhhvmnfe1oakm04ad2k6xbrh59 foreign key (user__id) references basic_application.user_;
alter table basic_application.user__UserGroup add constraint FKpctvagil9lsn2dovvt1fpfa5y foreign key (groups_id) references basic_application.UserGroup;
alter table basic_application.user__UserGroup add constraint FKgqeklcob49vjws6n5jkxbxg07 foreign key (persons_id) references basic_application.user_;
alter table basic_application.UserGroup_Authority add constraint FK9gql8awj70oo2bgxupgcu9qxf foreign key (authorities_id) references basic_application.Authority;
alter table basic_application.UserGroup_Authority add constraint FKrpwlh5niy92myi8fi0yiwr4ep foreign key (UserGroup_id) references basic_application.UserGroup;

--
-- Custom model
--