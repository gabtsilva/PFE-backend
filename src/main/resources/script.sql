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
                                                  tour_execution_id integer NOT NULL  REFERENCES snappies.tours_executions(tour_execution_id)
);

-- Insertion des utilisateurs
INSERT INTO snappies.users(email, firstname, lastname, phone_number, password, is_admin)
VALUES
    ('admin', 'Admin',  'User', '123456789', '$2a$10$iXDbSUmi5x1T84NgO6r0FuEPiDWLBhMFnbTmK5E4x5VtZecm1m6um', true),
    ('user', 'User',  'One', '987654321', '$2a$10$EzyRNcYwzu5DXUGoXnm.9u0IxS1TyZnR09wEosKM99ZZ7GWBemZ0S', false),
    ('user2', 'User2',  'One2', '987654321', '$2a$10$EzyRNcYwzu5DXUGoXnm.9u0IxS1TyZnR09wEosKM99ZZ7GWBemZ0S', false);

-- Insertion des tours
INSERT INTO snappies.tours(tour_name) VALUES
                                          ('Tour A'),
                                          ('Tour B'),
                                          ('Tour C');

-- Insertion des clients
INSERT INTO snappies.clients(client_address, client_name, phone_number, children_quantity, tour)
VALUES
    ('123 Main St, City1', 'Client One', '111222333', 2, 1),
    ('456 Oak St, City2', 'Client Two', '444555666', 3, 1),
    ('La bas', 'Client Three', '5984', 10, 1),
    ('Ici', 'Client Four', '48487', 10, 2),
    ('ou Ca ?','Client FIF','5555555', 555,2);

-- Insertion des véhicules
INSERT INTO snappies.vehicles(vehicle_name, plate, max_quantity)
VALUES
    ('Van A', 'ABC123', 10),
    ('Truck B', 'XYZ789', 15);

-- Insertion des exécutions de tour
INSERT INTO snappies.tours_executions(execution_date, state, delivery_person, vehicle_id, tour_id)
VALUES
    ('2023-12-13', 'prévue', null, 1, 1),
    ('2023-12-13', 'prévue', null, 2, 2);

-- Insertion des articles
INSERT INTO snappies.articles(article_name,pourcentage) VALUES
                                                            ('Article 1',0.1),
                                                            ('Article 2',0.2),
                                                            ('Article 3',0.5);

-- Insertion des commandes
INSERT INTO snappies.orders(client_id)
VALUES
    (1),
    (2),
    (3),
    (4),
    (5);

-- Insertion des lignes de commande
INSERT INTO snappies.orders_lines(order_id, article_id, planned_quantity, delivered_quantity, changed_quantity)
VALUES
    (1, 1, 5, 0, 2),--tour 1
    (1,2,10,0,15),--tour 1
    (2,1,10,0,10),--tour 1
    (2,3,2,0,2),--tour 1
    (3,1,20,0,15),--tour 1

    (4,2,20,20,20),--tour 2
    (5,3,2,0,2) ; -- tour 2

-- Insertion des commandes clients
INSERT INTO snappies.general_clients_orders(client_order, client_id, tour_id)
VALUES
    (1, 1, 1),
    (2,2,1),
    (3,3,1),
    (1,4,2),
    (2,5,2);


-- Insertion des lignes de commande d'exécution clients
INSERT INTO snappies.execution_clients_orders(delivered, general_client_order, tour_execution_id)
VALUES
    (false, 1, 1),
    (false,2,1),
    (false,3,1),
    (false,4,2),
    (false,5,2);









/*

SELECT
    a.article_id AS "articleId",
    a.article_name AS "articleName",
    SUM(ol.planned_quantity) AS "totalPlannedQuantity",
    SUM(ol.changed_quantity) AS "totalChangedQuantity",
    COALESCE(MAX(a.pourcentage), 0) AS "surplusPercentage"
FROM
    snappies.articles a
        INNER JOIN snappies.orders_lines ol ON a.article_id = ol.article_id
        INNER JOIN snappies.orders o ON ol.order_id = o.order_id
        INNER JOIN snappies.general_clients_orders gco ON o.client_id = gco.client_id
        INNER JOIN snappies.execution_clients_orders eco ON gco.general_client_order_id = eco.general_client_order
WHERE
        eco.tour_execution_id = 2 -- Remplacez [VotreTourExecutionId] par l'ID spécifique du tour d'exécution
GROUP BY
    a.article_id;

*/