create table event_categories (
	id serial primary key,
	displayName text not null,
	color varchar(255) not null
);
