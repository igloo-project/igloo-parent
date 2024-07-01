insert into user_ (id, creationdate, enabled, lastlogindate, lastupdatedate, locale,
                   passwordhash, username, email, firstname, lastname,
                   announcementinformation_lastactiondate, announcementinformation_open,
                   passwordinformation_lastupdatedate, passwordrecoveryrequest_creationdate,
                   passwordrecoveryrequest_initiator, passwordrecoveryrequest_token,
                   passwordrecoveryrequest_type)
values (-1, '2024-07-01 15:06:34.431', true, null, '2024-07-01 15:06:34.638', 'fr',
        '{bcrypt}$2a$10$SIP/xgO0qik2mFwk2rjzOOyVD3OYbWFsW.HclgHcVDnpIBCa1NLnK', 'basicUser', null, 'basicUser',
        'basicUser', null, true, null, null, null, null, null),
       (-2, '2024-07-01 15:06:34.722', true, null, '2024-07-01 15:06:34.863', 'fr',
        '{bcrypt}$2a$10$.YsJRrH4Yru6TV4eVwHvbeN4M/XfLqNyVzIOi7XB0WoBObLRM.9Du', 'basicUser2', null, 'basicUser2',
        'basicUser2', null, true, null, null, null, null, null),
       (-3, '2024-07-01 15:06:34.924', true, null, '2024-07-01 15:06:35.051', 'fr',
        '{bcrypt}$2a$10$oktDLIJLd78YicSTGEB7UuKofyK18kfvUyPSFW3WEVd7toCVG09zi', 'administrator', null, 'administrator',
        'administrator', null, true, null, null, null, null, null);

insert into basicuser values (-1),(-2);

insert into technicaluser values (-3);

INSERT INTO user__usergroup (users_id,groups_id)
VALUES (-1, -2),
       (-2, -2),
       (-3, -1);


insert into user__authority (user__id, authorities_id)
values (-1, -2),
       (-2, -2),
       (-3, -4);