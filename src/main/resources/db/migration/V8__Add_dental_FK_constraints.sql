alter table treatments
    add constraint FK_treatments_user_teeth
        foreign key (user_tooth_id) references user_teeth (id);
        
alter table user_teeth
    add constraint FK_userteeth_teeth
        foreign key (tooth_id) references teeth (id);
        
alter table user_teeth
    add constraint FK_userteeth_users
        foreign key (user_id) references users (id);

alter table complaints
    add constraint FK_complaints_user_teeth
        foreign key (user_tooth_id) references user_teeth (id);