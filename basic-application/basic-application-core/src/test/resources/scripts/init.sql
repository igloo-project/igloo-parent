INSERT INTO usergroup (id,description,"locked",name) VALUES
    (1,NULL,true,'Administrateurs'),
    (2,NULL,false,'Utilisateurs');

INSERT INTO authority (id,name) VALUES
    (1,'ROLE_ANONYMOUS'),
    (2,'ROLE_AUTHENTICATED'),
    (3,'ROLE_SYSTEM'),
    (4,'ROLE_ADMIN');

INSERT INTO usergroup_authority (usergroup_id,authorities_id) VALUES
    (1,4),
    (2,2);

