CREATE TABLE "people"
(
    "id"       serial PRIMARY KEY,
    "name"     varchar NOT NULL,
    "birthday" timestamp NOT NULL,
    "hp"       int NOT NULL,
    "mana"     int NOT NULL,
    "stamina"  int NOT NULL,
    "alcohol"  int NOT NULL,
    CHECK (
                hp >= 0
            AND mana >= 0
            AND stamina >= 0
            AND 0 <= alcohol AND alcohol <= 100
        )
);

CREATE TABLE "loyalty_levels"
(
    "level"       serial PRIMARY KEY,
    "sale"        int NOT NULL,
    "description" varchar NOT NULL,
    "money"       int NOT NULL,
    CHECK (sale >= 0 AND sale <= 100)
);


CREATE TABLE "customers"
(
    "id"            int PRIMARY KEY REFERENCES "people" ON UPDATE CASCADE ON DELETE CASCADE,
    "loyalty_level" int REFERENCES "loyalty_levels" ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE "ban_records"
(
    "id"          serial PRIMARY KEY,
    "customer_id" int UNIQUE REFERENCES "customers" ON UPDATE CASCADE ON DELETE CASCADE,
    "from"        timestamp NOT NULL,
    "to"          timestamp NOT NULL
);

-- TABLE "professions" -> enum Profession

CREATE TABLE "workers"
(
    "id"         int PRIMARY KEY REFERENCES "people" ON UPDATE CASCADE ON DELETE CASCADE,
    "profession" varchar NOT NULL
);


-- TABLE "actions" -> enum Role

-- TABLE "permissions" -> <program realization>

CREATE TABLE "food_types"
(
    "id"      serial PRIMARY KEY,
    "name"    varchar UNIQUE NOT NULL,
    "price"   int NOT NULL,
    "hp"      int NOT NULL,
    "mana"    int NOT NULL,
    "stamina" int NOT NULL,
    CHECK (
                hp >= 0
            AND mana >= 0
            AND "stamina" >= 0
        )
);

CREATE TABLE "mead_types"
(
    "id"      int PRIMARY KEY REFERENCES "food_types" ON UPDATE CASCADE ON DELETE CASCADE,
    "alcohol" int NOT NULL,
    CHECK (alcohol > 0 AND alcohol < 100)
);

CREATE TABLE "food_storage"
(
    "food_type" int PRIMARY KEY REFERENCES "food_types" ON UPDATE CASCADE ON DELETE CASCADE,
    "count"     int NOT NULL,
    CHECK (count > 0)
);

CREATE TABLE "orders"
(
    "id"          serial PRIMARY KEY,
    "customer_id" int REFERENCES "customers" ON UPDATE CASCADE ON DELETE CASCADE,
    "time"        timestamp NOT NULL,
    "closed"      boolean NOT NULL
);

CREATE TABLE "order_details"
(
    "order_id" int REFERENCES "orders" ON UPDATE CASCADE ON DELETE CASCADE,
    "food_id"  int REFERENCES "food_types" ON UPDATE CASCADE ON DELETE CASCADE,
    "count"    int NOT NULL,
    PRIMARY KEY ("order_id", "food_id"),
    CHECK ("count" > 0)
);

CREATE TABLE "users"
(
    "login"     varchar PRIMARY KEY,
    "password"  varchar,
    "person_id" integer REFERENCES "people" ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE "sessions"
(
    "session_id" varchar PRIMARY KEY,
    "user_login" varchar REFERENCES "users" ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE FUNCTION count_spent_money_by_customer(cust_id int) RETURNS int AS
$count_spent_money_by_customer$
DECLARE
    money_count int;
BEGIN
    SELECT sum(od.count * ft.price)
    INTO money_count
    FROM "orders" o
             JOIN order_details od on o.id = od.order_id
             JOIN food_types ft on ft.id = od.food_id
    WHERE o.customer_id = cust_id
      AND o.closed;
    RETURN money_count;
END;
$count_spent_money_by_customer$ LANGUAGE plpgsql;

CREATE FUNCTION update_loyalty_level() RETURNS trigger AS
$update_loyalty_level$
DECLARE
    sum     int;
    new_lvl int;
    old_lvl int;
BEGIN
    IF NEW.closed = true THEN
        sum := count_spent_money_by_customer(NEW.customer_id);
        SELECT max(ll.level)
        INTO new_lvl
        FROM loyalty_levels ll
        WHERE ll.money <= sum;

        SELECT c.loyalty_level
        INTO old_lvl
        FROM customers c
        WHERE c.id = NEW.customer_id;

        IF old_lvl != new_lvl THEN
            UPDATE customers SET loyalty_level = new_lvl WHERE id = NEW.customer_id;
        END IF;
    END IF;
    NEW.time := current_timestamp;
    RETURN NEW;
END;
$update_loyalty_level$ LANGUAGE plpgsql;

CREATE TRIGGER update_loyalty_level
    BEFORE UPDATE
    ON orders
    FOR ROW
EXECUTE PROCEDURE update_loyalty_level();

CREATE FUNCTION reduce_food_count_in_storage() RETURNS trigger AS
$reduce_food_count_in_storage$
DECLARE
    count_diff int;
    old_count  int;
    new_count  int;
BEGIN
    count_diff := NEW.count;
    old_count := (SELECT count FROM food_storage where food_type = NEW.food_id);
    new_count := old_count - count_diff;

    IF new_count = 0 THEN
        DELETE FROM food_storage WHERE food_type = NEW.food_id;
    ELSE
        UPDATE food_storage SET count = new_count WHERE food_type = NEW.food_id;
    END IF;

    RETURN NEW;
END;
$reduce_food_count_in_storage$ LANGUAGE plpgsql;

CREATE TRIGGER reduce_food_count_in_storage
    BEFORE INSERT
    ON order_details
    FOR ROW
EXECUTE PROCEDURE reduce_food_count_in_storage();

CREATE FUNCTION reduce_food_count_in_storage_by_diff() RETURNS trigger AS
$reduce_food_count_in_storage_by_diff$
DECLARE
    count_diff           int;
    old_count_in_storage int;
    new_count_in_storage int;
BEGIN
    count_diff := NEW.count - OLD.count;
    IF count_diff < 0 THEN
        RAISE EXCEPTION 'OPERATION OF DECREASING FOOD COUNT IS NOT SUPPORTED';
    END IF;
    old_count_in_storage := (SELECT count FROM food_storage where food_type = NEW.food_id);
    new_count_in_storage := old_count_in_storage - count_diff;

    IF new_count_in_storage = 0 THEN
        DELETE FROM food_storage WHERE food_type = NEW.food_id;
    ELSE
        UPDATE food_storage SET count = new_count_in_storage WHERE food_type = NEW.food_id;
    END IF;

    RETURN NEW;
END;
$reduce_food_count_in_storage_by_diff$ LANGUAGE plpgsql;

CREATE TRIGGER reduce_food_count_in_storage_by_diff
    BEFORE UPDATE
    ON order_details
    FOR ROW
EXECUTE PROCEDURE reduce_food_count_in_storage_by_diff();

CREATE FUNCTION check_closed_from_false_to_true() RETURNS trigger AS
$check_closed_from_false_to_true$
BEGIN
    IF NEW.closed = false AND OLD.closed = true THEN
        RAISE EXCEPTION 'can not open closed order';
    END IF;
    RETURN NEW;
END;
$check_closed_from_false_to_true$ LANGUAGE plpgsql;

CREATE TRIGGER check_closed_from_false_to_true
    BEFORE UPDATE
    ON orders
    FOR ROW
EXECUTE PROCEDURE check_closed_from_false_to_true();


INSERT INTO loyalty_levels (level, sale, description, money) VALUES (0, 0, 'Деревянный', 0);
INSERT INTO loyalty_levels (level, sale, description, money) VALUES (1, 3, 'Бронзовый', 50);
INSERT INTO loyalty_levels (level, sale, description, money) VALUES (2, 6, 'Серебряный', 200);
INSERT INTO loyalty_levels (level, sale, description, money) VALUES (3, 10, 'Золотой', 1000);