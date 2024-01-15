create table exchange_rate(
id text not null,
charCode text primary key,
nominal smallint not null,
name text not null,
value_rate numeric not null,
previous numeric not null
);
