drop database if exists DesktopControlSystem;
create database DesktopControlSystem;
use DesktopControlSystem;

create table classroom( cid int(10) auto_increment,
                        name char(30),
                        seats int(2),
                        guardIp char(15) unique,
                        forwardIp char(15) unique,
                        primary key (cid)
                        )character set = utf8;

create table lecture( lid int(10) auto_increment,
                      name char(30),
                      cid int(10),
                      startTime time,
                      endTime time,
                      weekday int(1),
                      primary key (lid),
                      foreign key (cid) references classroom(cid) on delete cascade
                      )character set = utf8;

create table student( sid char(10),
                      lid int(10),
                      primary key(sid, lid),
                      foreign key (lid) references lecture(lid) on delete cascade
                      )character set = utf8;

create table history( sid char(10),
                      cid int(10),
                      seat int(4),
                      startTime timestamp not null default CURRENT_TIMESTAMP,
                      endTime timestamp null default null,
                      foreign key (cid) references classroom(cid) on delete cascade
                      )character set = utf8;

create table tempOpen( sid char(10),
                       cid int(10),
                       startTime timestamp,
                       endTime timestamp,
                       foreign key (cid) references classroom(cid) on delete cascade
                       )character set = utf8;

create table card( cardId char(15),
                    sid char(10),
                    primary key (cardId)
                    )character set = utf8;