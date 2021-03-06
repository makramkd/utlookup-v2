-- Here is the schema for the database
-- we don't have to do any create schema business i don't think
-- The _id columns in each table are useful because
-- they are automatically created by the dbms so we don't
-- have to worry about any conflicts.

create table course(
    _id integer primary key autoincrement,
    code text unique not null,
    name text not null,
    prereqs text,
    coreqs text,
    exclusions text,
    description text not null,
    brdr text not null,
    recPrep text
);

create table department(
    _id integer primary key autoincrement,
    code text unique not null,
    name text not null
);

create table offers(
    _id integer primary key autoincrement,
    deptCode text not null,
    courseCode text not null,
    unique(deptCode, courseCode) on conflict replace,
    foreign key(deptCode) references department(code),
    foreign key(courseCode) references course(code)
);

create table instructor(
    _id integer primary key autoincrement,
    name text not null,
    deptCode text not null,
    foreign key(deptCode) references department(code)
);

create table depthead(
    _id integer primary key autoincrement,
    deptCode text not null,
    profId integer not null,
    unique(deptCode, profId) on conflict replace,
    foreign key(profId) references instructor(_id),
    foreign key(deptCode) references department(code)
);

create table meetingsection(
    _id integer primary key autoincrement,
    courseCode text not null,
    sectionCode text not null,
    location text,
    enrolIndicator text,
    times text,
    unique(courseCode, sectionCode) on conflict replace
);

create table teaches(
    _id integer primary key autoincrement,
    profId integer not null,
    courseCode text not null,
    sectionCode text not null,
    unique(profId, courseCode, sectionCode) on conflict replace,
    foreign key(courseCode, sectionCode) references meetingsection(courseCode, sectionCode)
);