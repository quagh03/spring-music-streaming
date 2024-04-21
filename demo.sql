DROP DATABASE IF EXISTS db_music_streaming;
CREATE DATABASE IF NOT EXISTS db_music_streaming;
USE db_music_streaming;
CREATE TABLE users(
	id int primary key auto_increment,
    username varchar(100) not null unique,
    hashed_password varchar(255) not null,
    email varchar(255) not null unique,
    created_at datetime DEFAULT NULL,
	updated_at datetime DEFAULT NULL,
	is_active tinyint(1) DEFAULT '1',
	date_of_birth date DEFAULT NULL
);
CREATE TABLE genres(
	id int primary key auto_increment,
    genres_name varchar(255),
    created_at datetime DEFAULT NULL,
	updated_at datetime DEFAULT NULL,
	is_active tinyint(1) DEFAULT '1'
);
CREATE TABLE artists(
	id int primary key auto_increment,
    artist_name varchar(255) not null,
    created_at datetime DEFAULT NULL,
	updated_at datetime DEFAULT NULL,
	is_active tinyint(1) DEFAULT '1'
);
CREATE TABLE albums(
	id int primary key auto_increment,
    album_name varchar(255) not null,
    artist_id int not null,
    genre_id int not null,
    thumbnail_url varchar(255) default null,
    created_at datetime DEFAULT NULL,
	updated_at datetime DEFAULT NULL,
	is_active tinyint(1) DEFAULT '1',
    foreign key(artist_id) references artists(id),
    foreign key(genre_id) references genres(id)
);
CREATE TABLE tracks(
	id int primary key auto_increment,
    title varchar(255),
    durration bigint,
    artist_id int not null,
    genre_id int not null,
    album_id int not null,
    thumbnail_url varchar(255) default null,
    track_url varchar(255) not null,
    likes int default 0,
    created_at datetime DEFAULT NULL,
	updated_at datetime DEFAULT NULL,
	is_active tinyint(1) DEFAULT '1',
    foreign key(artist_id) references artists(id),
    foreign key(genre_id) references genres(id),
    foreign key(album_id) references albums(id)
);
CREATE TABLE playlists(
	id int primary key auto_increment,
    title varchar(255) not null,
    creation_date DATETIME,
    user_id int not null,
    created_at datetime DEFAULT NULL,
	updated_at datetime DEFAULT NULL,
	is_active tinyint(1) DEFAULT '1',
    foreign key(user_id) references users(id)
);
CREATE TABLE playlist_details(
	id int primary key auto_increment,
    playlist_id int not null,
    track_id int not null,
    created_at datetime DEFAULT NULL,
	updated_at datetime DEFAULT NULL,
	is_active tinyint(1) DEFAULT '1',
    foreign key(playlist_id) references playlists(id),
    foreign key(track_id) references tracks(id)
);
CREATE TABLE track_likes(
	id int primary key auto_increment,
    track_id int not null,
    user_id int not null,
	created_at datetime DEFAULT NULL,
	updated_at datetime DEFAULT NULL,
	is_active tinyint(1) DEFAULT '1',    foreign key(user_id) references users(id),
    foreign key(track_id) references tracks(id)
);
CREATE TABLE rooms(
	id int primary key auto_increment,
    master_id int not null,
    title varchar(255),
    created_at datetime DEFAULT NULL,
	updated_at datetime DEFAULT NULL,
	is_active tinyint(1) DEFAULT '1',
    room_password varchar(255),
    foreign key(master_id) references users(id)
);
DELIMITER //
CREATE TRIGGER update_likes_after_insert
AFTER INSERT ON track_likes
FOR EACH ROW
BEGIN
    UPDATE tracks
    SET likes = likes + 1
    WHERE id = NEW.track_id;
END;
//
CREATE TRIGGER update_likes_after_delete
AFTER DELETE ON track_likes
FOR EACH ROW
BEGIN
    UPDATE tracks
    SET likes = likes - 1
    WHERE id = OLD.track_id;
END;
//
DELIMITER ;
DELIMITER $	
create procedure SelectTrackLike(
	in input_id int,
    out total int
)
begin 
	declare count int default 0;
    set count = (select count(track_id) from track_likes where track_id = input_id);
    set total = count;
end $
DELIMITER ;