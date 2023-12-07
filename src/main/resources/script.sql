DROP SCHEMA IF EXISTS snappies CASCADE;
CREATE SCHEMA snappies;

CREATE TABLE snappies.users(
                               mail VARCHAR(50) PRIMARY KEY CHECK (mail<>''),
                               firstname VARCHAR(50) NOT NULL CHECK (firstname<>''),
                               lastname VARCHAR(50) NOT NULL CHECK (lastname<>''),
                               phone_number VARCHAR(15) NOT NULL CHECK (users.phone_number<>''),
                               is_admin BOOLEAN NOT NULL DEFAULT false
);



CREATE TABLE snappies.credentials(
                                     mail VARCHAR(50) PRIMARY KEY CHECK (mail<>''),
                                     password VARCHAR(200) NOT NULL CHECK (password<>'')
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
                                          delivery_person varchar(50) NOT NULL REFERENCES snappies.users(mail),
                                          vehicle_id integer NOT NULL REFERENCES snappies.vehicles(vehicle_id),
                                          tour_id integer NOT NULL REFERENCES snappies.tours(tour_id)
);



CREATE TABLE snappies.commandes(
                                   commande_id SERIAL PRIMARY KEY,
                                   client_id integer NOT NULL REFERENCES snappies.clients (client_id),
                                   tour_execution integer REFERENCES snappies.tours_executions(tour_execution_id)
);


CREATE TABLE snappies.articles(
                                  article_id serial PRIMARY KEY,
                                  article_name varchar(100) NOT NULL CHECK (article_name <> '')
);


CREATE TABLE snappies.commandesLines(
                                        commande_id integer NOT NULL REFERENCES snappies.commandes(commande_id),
                                        article_id integer NOT NULL REFERENCES snappies.articles(article_id),
                                        planned_quantity integer NOT NULL CHECK (planned_quantity > 0),
                                        delivered_quantity integer NOT NULL CHECK (delivered_quantity >= 0),
                                        added_quantity integer NOT NULL CHECK (commandesLines.added_quantity >= 0),
                                        PRIMARY KEY (commande_id, article_id)
);

CREATE TABLE snappies.surplus(
                                 article_id integer NOT NULL REFERENCES snappies.articles(article_id),
                                 tour_execution_id integer NOT NULL REFERENCES snappies.tours_executions(tour_execution_id),
                                 percentage integer NOT NULL CHECK (percentage >=0),
                                 PRIMARY KEY (article_id, tour_execution_id)


);


CREATE TABLE snappies.clients_orders(
                                        client_order_id SERIAL PRIMARY KEY,
                                        client_order integer NOT NULL CHECK ( client_order > 0 ),
                                        delivered boolean NOT NULL DEFAULT false,
                                        client_id integer NOT NULL REFERENCES snappies.clients(client_id)
);


--Script test--
-- Insertion des utilisateurs
INSERT INTO snappies.users(mail, firstname, lastname, phone_number, is_admin)
VALUES
    ('admin@example.com', 'Admin',  'User', '123456789', true),
    ('user1@example.com', 'User',  'One', '987654321', false);

-- Insertion des tours
INSERT INTO snappies.tours(tour_name) VALUES
                                          ('Tour A'),
                                          ('Tour B');

-- Insertion des clients
INSERT INTO snappies.clients(client_address, client_name, phone_number, children_quantity, tour)
VALUES
    ('123 Main St, City1', 'Client One', '111222333', 2, 1),
    ('456 Oak St, City2', 'Client Two', '444555666', 3, 2);

-- Insertion des véhicules
INSERT INTO snappies.vehicles(vehicle_name, plate, max_quantity)
VALUES
    ('Van A', 'ABC123', 10),
    ('Truck B', 'XYZ789', 15);

-- Insertion des exécutions de tour
INSERT INTO snappies.tours_executions(execution_date, state, delivery_person, vehicle_id, tour_id)
VALUES
    ('2023-01-01', 'prévu', 'admin@example.com', 1, 1),
    ('2023-02-01', 'commencé', 'user1@example.com', 2, 2);

-- Insertion des commandes
INSERT INTO snappies.commandes(client_id, tour_execution)
VALUES
    (1, 1),
    (2, 2);

-- Insertion des articles
INSERT INTO snappies.articles(article_name) VALUES
                                                ('Article 1'),
                                                ('Article 2');

-- Insertion des lignes de commande
INSERT INTO snappies.commandesLines(commande_id, article_id, planned_quantity, delivered_quantity, added_quantity)
VALUES
    (1, 1, 5, 3, 2),
    (1, 2, 10, 8, 3),
    (2, 1, 8, 6, 1),
    (2, 2, 15, 12, 4);

-- Insertion des commandes clients
INSERT INTO snappies.clients_orders(client_order, delivered, client_id)
VALUES
    (101, false, 1),
    (102, true, 2);


-- Insertion des lignes de surplus
INSERT INTO snappies.surplus(article_id, tour_execution_id, percentage)
VALUES
    (1, 1, 5),
    (2, 1, 8),
    (1, 2, 3),
    (2, 2, 6);
