// TODO: remove when Diesel fix https://github.com/diesel-rs/diesel/issues/1785
#![allow(proc_macro_derive_resolution_fallback)]

#[macro_use]
extern crate diesel;
#[macro_use]
extern crate diesel_migrations;
extern crate dotenv;
#[macro_use]
extern crate lazy_static;
#[macro_use]
extern crate log;
extern crate log4rs;
extern crate monitor;
extern crate r2d2;
extern crate r2d2_diesel;
extern crate sha3;

use std::env;

use diesel::prelude::*;
use diesel::sqlite::SqliteConnection;
use dotenv::dotenv;
use r2d2::Pool;
use r2d2_diesel::ConnectionManager;

use models::{NewUser, User};

mod models;
mod schema;
mod users_dao;

lazy_static! {
    static ref pool: Pool<ConnectionManager<SqliteConnection>> = create_connection_pool();
}

pub fn create_connection_pool() -> Pool<ConnectionManager<SqliteConnection>> {
    dotenv().ok();
    let database_url = env::var("DATABASE_URL").expect("DATABASE_URL must be set");
    let manager = ConnectionManager::<SqliteConnection>::new(database_url);
    Pool::builder()
        .max_size(15)
        .build(manager)
        .expect("Failed to create pool.")
}

pub fn test() {
    println!("Hello world");
}

pub fn get_users() -> Vec<User> {
    let conn = pool.get().unwrap();
    users_dao::get_users(&conn)
}