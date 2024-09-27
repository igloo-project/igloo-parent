insert into announcement (id, creation_date, creation_subject_label, creation_subject_reference_id,
                          creation_subject_reference_type, description_fr, enabled,
                          interruption_enddatetime, interruption_startdatetime, modification_date,
                          modification_subject_label, modification_subject_reference_id,
                          modification_subject_reference_type, publication_enddatetime,
                          publication_startdatetime, title_fr, "type")
values (-1, '2024-01-01 00:00:00.000', 'User{id=3, username=BASIC_USERNAME_WITH_PERMISSIONS}', 3,
        'basicapp.back.business.user.model.User', 'testDescription', true, '2024-01-02 10:00:00.000',
        '2024-01-01 10:00:00.000', '2024-01-01 00:00:00.000', 'User{id=3, username=BASIC_USERNAME_WITH_PERMISSIONS}', 3,
        'basicapp.back.business.user.model.User', '2024-01-02 10:00:00.000', '2024-01-01 10:00:00.000',
        'testTitre', 'SERVICE_INTERRUPTION');
