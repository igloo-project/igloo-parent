insert into role (id, title)
values (-1, 'Default');

insert into role_permissions (role_id, permissions)
values (-1, 'GLOBAL_ROLE_READ'),
       (-1, 'GLOBAL_ANNOUNCEMENT_WRITE'),
       (-1, 'GLOBAL_ROLE_WRITE'),
       (-1, 'GLOBAL_REFERENCE_DATA_WRITE'),
       (-1, 'GLOBAL_ANNOUNCEMENT_READ'),
       (-1, 'GLOBAL_USER_READ'),
       (-1, 'GLOBAL_USER_WRITE'),
       (-1, 'GLOBAL_REFERENCE_DATA_READ');

insert into user_ (id, creationdate, enabled, lastupdatedate,
                   passwordhash, username, email, firstname, lastname,
                   announcementinformation_open,
                   "type")
values (-1, '2024-09-27 14:55:47.143', true, '2024-09-27 14:55:47.143',
        '{bcrypt}$2a$10$ii8ZZCYp9rt04V6Qawn1b.7MpQrd022rMr4sUTcJFCpc9ra8EL0Iu', 'BASIC_USERNAME_WITHOUT_PERMISSIONS',
        'user1@kobalt.fr', 'user1', 'user1', true, 'BASIC'),
       (-2, '2024-09-27 14:55:47.575', true, '2024-09-27 14:55:47.575',
        '{bcrypt}$2a$10$cvNfP3PFWq1jnIlXf8W4Y.KruhmBY31Gcc4pIG7B/Ocxq8Au0xkwa', 'ADMIN_USERNAME', 'user2@kobalt.fr',
        'user2', 'user2', true, 'TECHNICAL'),
       (-3, '2024-09-27 14:55:47.708', true, '2024-09-27 14:55:47.785',
        '{bcrypt}$2a$10$04tR9JXLBZ2TefixidU5j.6HCn5r.IE7w8vc0VJpvzcJg4vGm6bXa', 'BASIC_USERNAME_WITH_PERMISSIONS',
        'user3@kobalt.fr', 'user3', 'user3', true, 'BASIC');

INSERT INTO user__role (users_id, roles_id) VALUES
    (-3,-1);

