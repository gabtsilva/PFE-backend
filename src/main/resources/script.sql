DROP SCHEMA IF EXISTS snappies CASCADE;
CREATE SCHEMA snappies;

CREATE TABLE snappies.users(
                               email VARCHAR(50) PRIMARY KEY CHECK (email<>''),
                               password VARCHAR(50) NOT NULL CHECK (password<>''),
                               name VARCHAR(50) NOT NULL CHECK (name<>''),
                               phone_number VARCHAR(15) NOT NULL CHECK (users.phone_number<>''),
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
                                          state varchar(20) NOT NULL  CHECK ( state in ('prévu', 'commencé', 'finie') ),
                                          delivery_person varchar(50) NOT NULL REFERENCES snappies.users(email),
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
                                        PRIMARY KEY (commande_id, article_id)
);


CREATE TABLE snappies.clients_orders(
                                        client_order_id SERIAL PRIMARY KEY,
                                        client_order integer NOT NULL CHECK ( client_order > 0 ),
                                        delivered boolean NOT NULL DEFAULT false,
                                        client_id integer NOT NULL REFERENCES snappies.clients(client_id)
)

