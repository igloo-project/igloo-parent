-- this file use flyway placeholders to set ${type.text} type (as h2 do not handle unique constraint on clob/${type.text})

CREATE SCHEMA IF NOT EXISTS ${schema};

--
-- Base model
--
create sequence ${schema}.Announcement_id_seq start with 1 increment 1;
create sequence ${schema}.Authority_id_seq start with 1 increment 1;
create sequence ${schema}.City_id_seq start with 1 increment 1;
create sequence ${schema}.DataUpgradeRecord_id_seq start with 1 increment 1;
create sequence ${schema}.HistoryDifference_id_seq start with 1 increment 1;
create sequence ${schema}.HistoryLog_id_seq start with 1 increment 1;
create sequence ${schema}.Parameter_id_seq start with 1 increment 1;
create sequence ${schema}.QueuedTaskHolder_id_seq start with 1 increment 1;
create sequence ${schema}.user__id_seq start with 1 increment 1;
create sequence ${schema}.UserGroup_id_seq start with 1 increment 1;
create table ${schema}.Announcement (id int8 not null, creation_date timestamp not null, creation_subject_label ${type.text}, creation_subject_reference_id int8, creation_subject_reference_type varchar(255), creation_subject_serialized ${type.text}, description_en ${type.text}, description_fr ${type.text}, enabled boolean not null, interruption_endDateTime timestamp, interruption_startDateTime timestamp, modification_date timestamp not null, modification_subject_label ${type.text}, modification_subject_reference_id int8, modification_subject_reference_type varchar(255), modification_subject_serialized ${type.text}, publication_endDateTime timestamp not null, publication_startDateTime timestamp not null, title_en ${type.text}, title_fr ${type.text}, type varchar(255) not null, primary key (id));
create table ${schema}.Authority (id int8 not null, name ${type.text}, primary key (id));
create table ${schema}.Authority_customPermissionNames (Authority_id int8 not null, customPermissionNames ${type.text});
create table ${schema}.BasicUser (id int8 not null, primary key (id));
--TODO: igloo-boot
--create table ${schema}.City (id int8 not null, deleteable boolean not null, disableable boolean not null, editable boolean not null, enabled boolean not null, position int4 not null, label_en ${type.text}, label_fr ${type.text}, postalCode ${type.text} not null, primary key (id));
create table ${schema}.City (id int8 not null, deleteable boolean not null, disableable boolean not null, editable boolean not null, enabled boolean not null, position int4 not null, label_en ${type.text}, label_fr ${type.text}, primary key (id));
create table ${schema}.DataUpgradeRecord (id int8 not null, autoPerform boolean not null, done boolean not null, executionDate timestamp, name ${type.text} not null, primary key (id));
--TODO: igloo-boot
--create table ${schema}.HistoryDifference (id int8 not null, after_label ${type.text}, after_reference_id int8, after_reference_type varchar(255), after_serialized ${type.text}, before_label ${type.text}, before_reference_id int8, before_reference_type varchar(255), before_serialized ${type.text}, eventType varchar(255) not null, path_key_label ${type.text}, path_key_reference_id int8, path_key_reference_type varchar(255), path_key_serialized ${type.text}, path_path ${type.text} not null, parentDifference_id int8, parentLog_id int8, differences_ORDER int4, primary key (id));
create table ${schema}.HistoryDifference (id int8 not null, after_label ${type.text}, after_reference_id int8, after_reference_type varchar(255), after_serialized ${type.text}, before_label ${type.text}, before_reference_id int8, before_reference_type varchar(255), before_serialized ${type.text}, eventType varchar(255) not null, path_key_label ${type.text}, path_key_reference_id int8, path_key_reference_type varchar(255), path_key_serialized ${type.text}, path_path ${type.text} not null, parentDifference_id int8, parentLog_id int8, primary key (id));
create table ${schema}.HistoryLog (id int8 not null, comment ${type.text}, date timestamp not null, eventType varchar(255) not null, mainObject_label ${type.text}, mainObject_reference_id int8, mainObject_reference_type varchar(255), mainObject_serialized ${type.text}, object1_label ${type.text}, object1_reference_id int8, object1_reference_type varchar(255), object1_serialized ${type.text}, object2_label ${type.text}, object2_reference_id int8, object2_reference_type varchar(255), object2_serialized ${type.text}, object3_label ${type.text}, object3_reference_id int8, object3_reference_type varchar(255), object3_serialized ${type.text}, object4_label ${type.text}, object4_reference_id int8, object4_reference_type varchar(255), object4_serialized ${type.text}, subject_label ${type.text}, subject_reference_id int8, subject_reference_type varchar(255), subject_serialized ${type.text}, primary key (id));
create table ${schema}.Parameter (id int8 not null, name ${type.text} not null, stringValue ${type.text}, primary key (id));
create table ${schema}.QueuedTaskHolder (id int8 not null, creationDate timestamp not null, endDate timestamp, name ${type.text} not null, queueId ${type.text}, report ${type.text}, result varchar(255), serializedTask ${type.text} not null, stackTrace ${type.text}, startDate timestamp, status varchar(255) not null, taskType ${type.text} not null, triggeringDate timestamp, optLock int4, primary key (id));
create table ${schema}.TechnicalUser (id int8 not null, primary key (id));
create table ${schema}.user_ (id int8 not null, creationDate timestamp not null, enabled boolean not null, lastLoginDate timestamp, lastUpdateDate timestamp not null, locale varchar(255), passwordHash ${type.text}, username ${type.text} not null, email ${type.text}, firstName ${type.text} not null, lastName ${type.text} not null, announcementInformation_lastActionDate timestamp, announcementInformation_open boolean not null, passwordInformation_lastUpdateDate timestamp, passwordRecoveryRequest_creationDate timestamp, passwordRecoveryRequest_initiator varchar(255), passwordRecoveryRequest_token ${type.text}, passwordRecoveryRequest_type varchar(255), primary key (id));
create table ${schema}.user__Authority (user__id int8 not null, authorities_id int8 not null, primary key (user__id, authorities_id));
create table ${schema}.user__passwordInformation_history (user__id int8 not null, passwordInformation_history ${type.text}, history_ORDER int4 not null, primary key (user__id, history_ORDER));
create table ${schema}.user__UserGroup (users_id int8 not null, groups_id int8 not null, primary key (users_id, groups_id));
create table ${schema}.UserGroup (id int8 not null, description ${type.text}, locked boolean not null, name ${type.text}, primary key (id));
create table ${schema}.UserGroup_Authority (UserGroup_id int8 not null, authorities_id int8 not null, primary key (UserGroup_id, authorities_id));
--TODO: igloo-boot
--alter table ${schema}.City add constraint UKcici6ao6snb79g0i2ebsix408 unique (label_fr, postalCode);
alter table ${schema}.City add constraint UKcici6ao6snb79g0i2ebsix408 unique (label_fr);
alter table ${schema}.DataUpgradeRecord add constraint UK_6q54k3x0axoc3n8ns55emwiev unique (name);
create index idx_HistoryDifference_parentLog on ${schema}.HistoryDifference (parentLog_id);
create index idx_HistoryDifference_parentDifference on ${schema}.HistoryDifference (parentDifference_id);
alter table ${schema}.Parameter add constraint UK_k31gbcltpas6ux95qpk19o6q5 unique (name);
alter table ${schema}.user_ add constraint UK_q1sdxrqyk0i3sw35q3m92sx3m unique (username);
alter table ${schema}.Authority_customPermissionNames add constraint FK6uqor94s0128ryjrrdw84f67x foreign key (Authority_id) references ${schema}.Authority;
alter table ${schema}.BasicUser add constraint FKhjnq1hls89k8qbs2hpap2i09j foreign key (id) references ${schema}.user_;
alter table ${schema}.HistoryDifference add constraint FKbs6nv5pjsfuj7na52g16pfbrw foreign key (parentDifference_id) references ${schema}.HistoryDifference;
alter table ${schema}.HistoryDifference add constraint FK77dlplmm50qgjbl1cqv4nrgoi foreign key (parentLog_id) references ${schema}.HistoryLog;
alter table ${schema}.TechnicalUser add constraint FKq1c424ixlnmkb5c8w1bdddybj foreign key (id) references ${schema}.user_;
alter table ${schema}.user__Authority add constraint FKhoyhws618rm4v2rjvm0x9uuhk foreign key (authorities_id) references ${schema}.Authority;
alter table ${schema}.user__Authority add constraint FK1nrpbko4mct39gq143ef5jwq0 foreign key (user__id) references ${schema}.user_;
alter table ${schema}.user__passwordInformation_history add constraint FKhhvmnfe1oakm04ad2k6xbrh59 foreign key (user__id) references ${schema}.user_;
alter table ${schema}.user__UserGroup add constraint FKpctvagil9lsn2dovvt1fpfa5y foreign key (groups_id) references ${schema}.UserGroup;
alter table ${schema}.user__UserGroup add constraint FKi09rq0mpjvl80ck9tglp995d6 foreign key (users_id) references ${schema}.user_;
alter table ${schema}.UserGroup_Authority add constraint FK9gql8awj70oo2bgxupgcu9qxf foreign key (authorities_id) references ${schema}.Authority;
alter table ${schema}.UserGroup_Authority add constraint FKrpwlh5niy92myi8fi0yiwr4ep foreign key (UserGroup_id) references ${schema}.UserGroup;

--
-- Custom model
--