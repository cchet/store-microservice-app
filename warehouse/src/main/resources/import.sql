-- ProductType
insert into ProductType(id, taxPercent) values('FOOD', '10.00');
insert into ProductType(id, taxPercent) values('ELECTRONICS', '20.00');
-- Product
insert into Product(id, name, count, price, product_type_id) values('0000001', 'mac-book-prod-14', 10, '2000.00', 'ELECTRONICS');
insert into Product(id, name, count, price, product_type_id) values('0000002', 'mac-book-prod-16', 10, '3000.00', 'ELECTRONICS');
insert into Product(id, name, count, price, product_type_id) values('0000003', 'mac-book-prod-18', 10, '4000.00', 'ELECTRONICS');
insert into Product(id, name, count, price, product_type_id) values('0000004', 'red-bull', 10, '1.50', 'FOOD');
insert into Product(id, name, count, price, product_type_id) values('0000005', 'red-bull-cola', 10, '1.60', 'FOOD');
insert into Product(id, name, count, price, product_type_id) values('0000006', 'leberkäs-semmel', 10, '2.00', 'FOOD');
