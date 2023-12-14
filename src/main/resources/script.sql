DROP SCHEMA IF EXISTS snappies CASCADE;
CREATE SCHEMA snappies;

CREATE TABLE snappies.users(
                               email VARCHAR(50) PRIMARY KEY CHECK (email<>''),
                               firstname VARCHAR(50) NOT NULL CHECK (firstname<>''),
                               lastname VARCHAR(50) NOT NULL CHECK (lastname<>''),
                               phone_number VARCHAR(15) NOT NULL CHECK (users.phone_number<>''),
                               password VARCHAR(200) NOT NULL CHECK (password<>''),
                               is_admin BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE snappies.tours(
                               tour_id SERIAL PRIMARY KEY,
                               tour_name varchar(50) NOT NULL CHECK (tour_name<>'')
);

CREATE TABLE snappies.clients(
                                 client_id SERIAL PRIMARY KEY,
                                 client_address varchar(200) NOT NULL CHECK (client_address <> ''),
                                 client_name varchar(50) NOT NULL CHECK (client_name <>''),
                                 phone_number varchar(15) NOT NULL CHECK (phone_number <>''),
                                 children_quantity integer NOT NULL CHECK(children_quantity > 0),
                                 tour integer NOT NULL REFERENCES snappies.tours (tour_id)
);

CREATE TABLE snappies.vehicles(
                                  vehicle_id SERIAL PRIMARY KEY,
                                  vehicle_name varchar(100) CHECK ( vehicle_name <> '' ),
                                  plate varchar(15) CHECK ( plate <> '' ),
                                  max_quantity float NOT NULL CHECK (max_quantity > 0)
);

CREATE TABLE snappies.tours_executions(
                                          tour_execution_id SERIAL PRIMARY KEY,
                                          execution_date date NOT NULL,
                                          state varchar(20) NOT NULL  CHECK ( state in ('prévue', 'commencée', 'finie') ),
                                          delivery_person varchar(50) REFERENCES snappies.users(email),
                                          vehicle_id integer REFERENCES snappies.vehicles(vehicle_id),
                                          tour_id integer NOT NULL REFERENCES snappies.tours(tour_id)
);

CREATE TABLE snappies.orders(
                                order_id SERIAL PRIMARY KEY,
                                client_id integer NOT NULL REFERENCES snappies.clients (client_id)
);

CREATE TABLE snappies.articles(
                                  article_id serial PRIMARY KEY,
                                  article_name varchar(100) NOT NULL CHECK (article_name <> ''),
                                  pourcentage float not null CHECK ( pourcentage >= 0 )
);

CREATE TABLE snappies.orders_lines(
                                      order_id integer NOT NULL REFERENCES snappies.orders(order_id),
                                      article_id integer NOT NULL REFERENCES snappies.articles(article_id),
                                      planned_quantity float NOT NULL CHECK (planned_quantity >= 0),
                                      delivered_quantity float NOT NULL CHECK (delivered_quantity >= 0) DEFAULT 0,
                                      changed_quantity float NOT NULL DEFAULT 0,
                                      PRIMARY KEY (order_id, article_id)
);


CREATE TABLE snappies.general_clients_orders(
                                                general_client_order_id SERIAL PRIMARY KEY,
                                                client_order integer NOT NULL CHECK ( client_order > 0 ),
                                                client_id integer NOT NULL REFERENCES snappies.clients(client_id),
                                                tour_id integer NOT NULL  REFERENCES snappies.tours(tour_id),
                                                unique(tour_id, client_id)

);

CREATE TABLE snappies.execution_clients_orders(
                                                  execution_client_order_id SERIAL PRIMARY KEY,
                                                  delivered boolean NOT NULL DEFAULT false,
                                                  general_client_order integer REFERENCES snappies.general_clients_orders(general_client_order_id),
                                                  tour_execution_id integer NOT NULL  REFERENCES snappies.tours_executions(tour_execution_id),
                                                  unique (general_client_order, tour_execution_id)
);

-- Insertion des utilisateurs
INSERT INTO snappies.users(email, firstname, lastname, phone_number, password, is_admin)
VALUES
    ('admin', 'Admin',  'User', '123456789', '$2a$10$iXDbSUmi5x1T84NgO6r0FuEPiDWLBhMFnbTmK5E4x5VtZecm1m6um', true),
    ('user', 'User',  'One', '987654321', '$2a$10$EzyRNcYwzu5DXUGoXnm.9u0IxS1TyZnR09wEosKM99ZZ7GWBemZ0S', false),
    ('user2', 'User2',  'One2', '987654321', '$2a$10$EzyRNcYwzu5DXUGoXnm.9u0IxS1TyZnR09wEosKM99ZZ7GWBemZ0S', false);

-- Insertion des tours
INSERT INTO snappies.tours(tour_name) VALUES
                                          ('Villes de la région de Charleroi'),
                                          ('La Hulpe)'),
                                          ('Bruxelles');

-- Insertion des clients
INSERT INTO snappies.clients(client_address, client_name, phone_number, children_quantity, tour)
VALUES
    ('Rue Francisco Ferrer 19 boite 3, 6181 Gouy-Lez-Piéton', 'Rêverie', '04123456', 12, 1),
    ('Rue de la Vielle Place 51, 6001 Marcinelle','Les ptits loups','04123456',15 ,1),
    ('Chaussée de Nivelles 212, 6041 gosselies','L arbre à cabane','04123456',8, 1),
    ('Rue de Tamines 18, 6224 Wanfercée-Baulet','Les lutins','04123456',22, 1),
    ('Rue des Combattants, 59, 1310 La Hulpe','Les Tiffins','04123456',12, 2),
    ('Rue Souveraine 48, 1050 Bruxelles','Cardinal Mercier','04123456',21, 3),
    ('Av. Ducpétiaux 16, 1060 Saint-Gilles','Les Poussins','04123456',7, 3),
    ('Chaussée de Boisfort 40, 1050 Ixelles','Saint Joseph','04123456',11, 3);

-- Insertion des véhicules
INSERT INTO snappies.vehicles(vehicle_name, plate, max_quantity)
VALUES
    ('Van A', 'ABC123', 10),
    ('Truck B', 'XYZ789', 15);

-- Insertion des exécutions de tour
INSERT INTO snappies.tours_executions(execution_date, state, delivery_person, vehicle_id, tour_id)
VALUES
    ('2023-12-14', 'prévue', null, 1, 1),
    ('2023-12-14', 'prévue', null, 2, 2),
    ('2023-12-14', 'prévue', null, 1, 3);

-- Insertion des articles
INSERT INTO snappies.articles(article_name,pourcentage) VALUES
                                                            ('Langes S',0.1),
                                                            ('Langes M',0.2),
                                                            ('Langes L',0.5),
                                                            ('Inserts', 0.1),
                                                            ('Sacs-poubelle', 0.5),
                                                            ('Gants de toilette', 0.2);

-- Insertion des commandes
INSERT INTO snappies.orders(client_id)
VALUES
    (1),
    (2),
    (3),
    (4),
    (5),
    (6),
    (7),
    (8);

-- Insertion des lignes de commande
INSERT INTO snappies.orders_lines(order_id, article_id, planned_quantity, delivered_quantity, changed_quantity)
VALUES
    (1, 1, 1, 0, 1),
    (1,2,3,0,3),
    (1,3,1,0,1),
    (1,4,1,0,1),
    (1,5,10,0,10),
    (1,6,2,0,2),

    (2,2,3,0,3),
    (2,3,1,0,1),
    (2,5,6,0,6),

    (3,1,1,0,1),
    (3,2,2,0,2),
    (3,3,1,0,1),
    (3,4,1,0,1),
    (3,5,8,0,8),

    (4,1,1,0,1),
    (4,2,5,0,5),
    (4,3,2,0,2),
    (4,4,2,0,2),
    (4,5,12,0,12),
    (4,6,1,0,1),

    (5,1,1,0,1),
    (5,2,3,0,3),
    (5,3,2,0,2),
    (5,4,2,0,2),
    (5,5,12,0,12),

    (6,1,0.5,0,0.5),
    (6,2,5,0,5),
    (6,3,1,0,1),
    (6,4,1,0,1),
    (6,5,14,0,14),

    (7,2,1,0,1),
    (7,3,0.5,0,0.5),
    (7,5,4,0,4),

    (8,1,1,0,1),
    (8,2,3,0,3),
    (8,3,1.5,0,1.5),
    (8,4,1,0,1),
    (8,5,14,0,14) ; -- tour 2

-- Insertion des commandes clients
INSERT INTO snappies.general_clients_orders(client_order, client_id, tour_id)
VALUES
    (1,1,1),
    (2,2,1),
    (3,3,1),
    (4,4,1),
    (1,5,2),
    (1,6,3),
    (2,7,3),
    (3,8,3);


-- Insertion des lignes de commande d'exécution clients
INSERT INTO snappies.execution_clients_orders(delivered, general_client_order, tour_execution_id)
VALUES
    (false,1,1),
    (false,2,1),
    (false,3,1),
    (false,4,1),
    (false,5,2),
    (false,6,3),
    (false,7,3),
    (false,8,3);


