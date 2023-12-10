-- DROP SEQUENCE if EXISTS app_users_id_seq cascade;
-- DROP sequence if exists assessment_item_id_seq cascade;
-- drop sequence if exists subjects_id_seq cascade;
--
-- drop table if exists app_users cascade;
-- drop table if exists subject_students cascade;
-- drop table if exists subjects cascade;
-- drop table if exists assessment_items cascade;

create table if not exists app_users
(
    id           serial primary key not null,
    email        varchar(200)       not null unique,
    password     varchar(64),
    first_name   varchar(100)       not null,
    last_name    varchar(200)       not null,
    father_name  varchar(200),
    role         varchar(30)        not null,
    created_date date               not null,
    is_approved  bool               not null default false
);

create table if not exists subjects
(
    id         serial primary key not null,
    name       varchar(100)       not null,
    teacher_id int references app_users (id)
);

create table if not exists subject_students
(
    student_id int references app_users (id),
    subject_id int references subjects (id)
);

create table assessment_items
(
    id            serial primary key not null,
    examiner_name varchar(200),
    type          varchar(100),
    grade         int,
    exam_date     date,
    student_id    int references app_users (id) on delete cascade on update cascade,
    teacher_id    int references app_users (id),
    subject_id    int references subjects (id)
);

INSERT INTO app_users (email, password, first_name, last_name, father_name, role, created_date, is_approved)
values ('admin@admin.com', '$2y$10$s8WXZIdH8bZVN0Hl9J5KEeSm9BVAJMqFiXDCjPBMlZvhg/5Re0xDG', 'Admin', 'Adminov',
        'Adminovich', 'ADMIN', '2018-01-01'::date, true),
       ('teacher@teacher.com', '$2y$10$3lqCwSptPUatbTgAOsmUDex7ESor2EEuXOYTasdGevs6mtRQtFX4a', 'Teacher', 'Teacherov',
        'Teacherovich', 'TEACHER', '2018-01-01'::date, true),
       ('teacher2@teacher.com', '$2y$10$3lqCwSptPUatbTgAOsmUDex7ESor2EEuXOYTasdGevs6mtRQtFX4a', 'Teacher', 'Teacherov',
        'Teacherovich', 'TEACHER', '2018-01-01'::date, true),
       ('student@student.com', '$2y$10$EeDUGBP74IEDRmUknn6k5.JSeTxBXB9EyRh9K0jmFCZg3DelxHb8y', 'Student', 'Studentov',
        'Studentovich', 'STUDENT', '2018-01-01'::date, true);

insert into subjects (name, teacher_id)
VALUES ('Разработка клиент серверных приложений', 2),
       ('Aрхитектура клиент серверных приложений', 3),
       ('Интерфейсы прикладного программироани', 2);

insert into subject_students (student_id, subject_id)
VALUES (4, 1),
       (4, 2),
       (4, 3);