-- Your SQL goes here
CREATE TABLE users (
                       id       INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                       username TEXT                              NOT NULL,
                       password TEXT                              NOT NULL,
                       is_admin BOOLEAN                           NOT NULL DEFAULT 0
)