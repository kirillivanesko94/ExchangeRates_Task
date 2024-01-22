create table exchange_rate(
id text not null,
num_code text not null,
char_code text primary key,
nominal smallint not null,
name text not null,
value_rate numeric not null,
previous numeric not null
);
