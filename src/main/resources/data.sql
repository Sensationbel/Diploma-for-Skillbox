insert into fields (id, name, selector, weight) values (1, 'title', 'title', 1.0);
insert into fields (id, name, selector, weight) values (2, 'body', 'body', 0.8);
ALTER TABLE pages CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci
