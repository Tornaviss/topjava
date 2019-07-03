DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id) VALUES
  (TIMESTAMP '1999-01-08 10:00:00', 'Завтрак', 1000, 100000),
  (TIMESTAMP '1999-01-09 14:00:00', 'Обед', 1000, 100000),
  (TIMESTAMP '1999-01-08 10:00:00', 'Завтрак', 1000, 100001),
  (TIMESTAMP '1999-01-09 14:00:00', 'Обед', 1000, 100001)

