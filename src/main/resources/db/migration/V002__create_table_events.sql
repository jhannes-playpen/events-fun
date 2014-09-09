create table events (
	id serial primary key,
	displayName text not null,
	category_id integer null,
	start_date date null,
	end_date date null
);
