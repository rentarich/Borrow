INSERT INTO item (title, description, category) VALUES ('Basketball ball','Ball for playing basketball.','Sports');
INSERT INTO item (title, description, category) VALUES ('Poker cards','Cards for playing poker.','Board game');
INSERT INTO item (title, description, category) VALUES ('Remi cards','Cards for remi.','Board game');
INSERT INTO item (title, description, category) VALUES ('Golf gear','Gear for playing golf.','Sports');
INSERT INTO item (title, description, category) VALUES ('Fishing boots','Boots for fishing.','Water sports');
INSERT INTO item (title, description, category) VALUES ('Tent','Tent for 2 persons.','Nature');
INSERT INTO item (title, description, category) VALUES ('Sup','Sup for lake, sea.','Water sports');
INSERT INTO item (title, description, category) VALUES ('Climbing gear','Gear for climbing.','Sports');
INSERT INTO item (title, description, category) VALUES ('Parachute','Perfect for sky-diving.','Sports');

INSERT INTO person (username, email, role) VALUES ('crazyChica','crazychica@gmail.com','user');
INSERT INTO person (username, email, role) VALUES ('crazyChichito','crazychico@gmail.com','user');


INSERT INTO borrow (from_date, to_date, id_person,id_item,returned) VALUES ('2021-10-29','2021-10-31',1,9, false);
INSERT INTO borrow (from_date, to_date, id_person,id_item,returned) VALUES ('2021-10-29','2021-10-31',1,8, true);
INSERT INTO borrow (from_date, to_date, id_person,id_item,returned) VALUES ('2021-10-29','2021-10-31',2,6, false);
