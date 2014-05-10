/* insert data into classroom */
insert into classroom(name, seats, guardIp, forwardIp) values ("西1-101", 50, "192.168.0.2", "192.168.1.2");
insert into classroom(name, seats, guardIp, forwardIp) values ("西1-102", 40, "192.168.0.3", "192.168.1.3");
insert into classroom(name, seats, guardIp, forwardIp) values ("西1-103", 30, "192.168.0.4", "192.168.1.4");
insert into classroom(name, seats, guardIp, forwardIp) values ("西1-104", 55, "192.168.0.5", "192.168.1.5");
insert into classroom(name, seats, guardIp, forwardIp) values ("西1-105", 20, "192.168.0.6", "192.168.1.6");
insert into classroom(name, seats, guardIp, forwardIp) values ("西1-106", 45, "192.168.0.7", "192.168.1.7");
insert into classroom(name, seats, guardIp, forwardIp) values ("西1-107", 60, "192.168.0.8", "192.168.1.8");
insert into classroom(name, seats, guardIp, forwardIp) values ("西2-101", 100, "192.168.0.9", "192.168.1.9");

/* insert data into lecture */
insert into lecture(name, cid, startTime, endTime, weekday) values ("课程1", 1, "08:00:00", "08:45:00", 1);
insert into lecture(name, cid, startTime, endTime, weekday) values ("课程2", 1, "09:00:00", "09:45:00", 1);
insert into lecture(name, cid, startTime, endTime, weekday) values ("课程3", 1, "10:00:00", "10:45:00", 2);
insert into lecture(name, cid, startTime, endTime, weekday) values ("课程4", 2, "08:00:00", "08:45:00", 3);
insert into lecture(name, cid, startTime, endTime, weekday) values ("课程5", 3, "08:00:00", "08:45:00", 4);
insert into lecture(name, cid, startTime, endTime, weekday) values ("课程6", 4, "08:00:00", "08:45:00", 1);
insert into lecture(name, cid, startTime, endTime, weekday) values ("课程7", 4, "09:00:00", "09:45:00", 2);
insert into lecture(name, cid, startTime, endTime, weekday) values ("课程8", 5, "08:00:00", "08:45:00", 1);
insert into lecture(name, cid, startTime, endTime, weekday) values ("课程9", 6, "08:00:00", "08:45:00", 5);

/* insert data into student */
insert into student(sid, lid) values (3100102777, 1);
insert into student(sid, lid) values (3100102776, 1);
insert into student(sid, lid) values (3100102775, 1);
insert into student(sid, lid) values (3100102774, 1);
insert into student(sid, lid) values (3100102773, 1);
insert into student(sid, lid) values (3100102772, 1);
insert into student(sid, lid) values (3100102771, 1);
insert into student(sid, lid) values (3100102777, 2);
insert into student(sid, lid) values (3100102777, 3);
insert into student(sid, lid) values (3100102777, 4);
insert into student(sid, lid) values (3100102777, 5);

/* insert data into card */
insert into card(cardId, sid) values (252005036092, 3100102777);
insert into card(cardId, sid) values (252005036093, 3100102776);
insert into card(cardId, sid) values (252005036094, 3100102775);
insert into card(cardId, sid) values (252005036095, 3100102774);
insert into card(cardId, sid) values (252005036096, 3100102773);
insert into card(cardId, sid) values (252005036097, 3100102772);
insert into card(cardId, sid) values (252005036098, 3100102771);
insert into card(cardId, sid) values (252005036099, 3100102770);
insert into card(cardId, sid) values (252005036100, 3100102779);

/* insert data into history */
insert into history(sid, cid, seat, startTime, endTime) values (3100102777, 1, 1, "2014-01-01 08:00:00", "2014-01-01 08:40:00");
insert into history(sid, cid, seat, startTime, endTime) values (3100102778, 1, 2, "2014-01-01 08:00:00", "2014-01-01 08:50:00");
insert into history(sid, cid, seat, startTime, endTime) values (3100102779, 1, 3, "2014-01-01 08:00:00", "2014-01-01 08:20:00");
insert into history(sid, cid, seat, startTime, endTime) values (3100102787, 1, 4, "2014-01-01 08:00:00", "2014-01-01 08:30:00");
insert into history(sid, cid, seat, startTime, endTime) values (3100102877, 1, 5, "2014-01-01 08:00:00", "2014-01-01 08:45:00");

insert into history(sid, cid, seat, startTime, endTime) values (3100102777, 2, 1, "2014-01-05 08:00:00", "2014-01-05 08:40:00");
insert into history(sid, cid, seat, startTime, endTime) values (3100102778, 2, 2, "2014-01-05 08:00:00", "2014-01-05 08:50:00");
insert into history(sid, cid, seat, startTime, endTime) values (3100102779, 2, 3, "2014-01-05 08:00:00", "2014-01-05 08:20:00");
insert into history(sid, cid, seat, startTime, endTime) values (3100102787, 2, 4, "2014-01-05 08:00:00", "2014-01-05 08:30:00");
insert into history(sid, cid, seat, startTime, endTime) values (3100102877, 2, 5, "2014-01-05 08:00:00", "2014-01-05 08:45:00");

insert into history(sid, cid, seat, startTime, endTime) values (3100102777, 4, 1, "2014-03-01 08:00:00", "2014-03-01 08:40:00");
insert into history(sid, cid, seat, startTime, endTime) values (3100102778, 4, 2, "2014-03-01 08:00:00", "2014-03-01 08:50:00");
insert into history(sid, cid, seat, startTime, endTime) values (3100102779, 4, 3, "2014-03-01 08:00:00", "2014-03-01 08:20:00");
insert into history(sid, cid, seat, startTime, endTime) values (3100102787, 4, 4, "2014-03-01 08:00:00", "2014-03-01 08:30:00");
insert into history(sid, cid, seat, startTime, endTime) values (3100102877, 4, 5, "2014-03-01 08:00:00", "2014-03-01 08:45:00");