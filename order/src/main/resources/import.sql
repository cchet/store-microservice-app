insert into orders(id, username, creationDate, updatedDate, state) values('1c4d3225-2640-4fe8-82ce-b8f017f1f55b', 'customer', current_timestamp, current_timestamp, 'PLACED');
insert into order_items(order_id, productId, count, price, rabat) values('1c4d3225-2640-4fe8-82ce-b8f017f1f55b', '0000001', 1, '2000.00', '0.00');
insert into order_items(order_id, productId, count, price, rabat) values('1c4d3225-2640-4fe8-82ce-b8f017f1f55b', '0000002', 1, '3000.00', '0.00');
insert into order_items(order_id, productId, count, price, rabat) values('1c4d3225-2640-4fe8-82ce-b8f017f1f55b', '0000003', 1, '4000.00', '0.00');
insert into orders(id, username, creationDate, updatedDate, state) values('f47ddd77-be26-47d0-99ac-472bf02a4291', 'customer', current_timestamp, current_timestamp, 'FULLFULLED');
insert into order_items(order_id, productId, count, price, rabat) values('f47ddd77-be26-47d0-99ac-472bf02a4291', '0000004', 2, '1.50', '0.00');
insert into order_items(order_id, productId, count, price, rabat) values('f47ddd77-be26-47d0-99ac-472bf02a4291', '0000005', 3, '1.60', '0.00');
insert into order_items(order_id, productId, count, price, rabat) values('f47ddd77-be26-47d0-99ac-472bf02a4291', '0000006', 4, '2.00', '0.00');