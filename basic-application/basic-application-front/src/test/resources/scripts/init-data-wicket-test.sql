insert into user_ (id, creation_date, enabled, modification_date, locale,
                                          passwordhash, username, emailaddress, firstname, lastname,
                                          announcementinformation_open,
                                          "type")
values (-1, '2024-09-27 17:10:29.287', true, '2024-09-27 17:10:29.287', 'fr',
        '{bcrypt}$2a$10$fSKK9CAACyoXgvAdOelBVeC1Yt6jjDYjOvvST3gIG1OdwbkY/xCLS', 'basicUser', 'user1@kobalt.fr', 'user1',
        'user1', true, 'BASIC'),
       (-2, '2024-09-27 17:10:29.555', true, '2024-09-27 17:10:29.555', 'fr',
        '{bcrypt}$2a$10$bd4OdJRn2BNBjXNdEUo0RexB81QatFPSb1rlPWolBSCoO8UdFvCoK', 'basicUser2', 'user2@kobalt.fr',
        'user2', 'user2', true, 'BASIC'),
       (-3, '2024-09-27 17:10:29.669', true, '2024-09-27 17:10:29.669', 'fr',
        '{bcrypt}$2a$10$jKdBem7jpJ6hjVoyx4nPO.xOeAcKnQILlUWV8NajOnZgRZXTARXEO', 'technicalUser', 'user3@kobalt.fr',
        'user3', 'user3', true, 'TECHNICAL');
