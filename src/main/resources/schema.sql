-- Create artists table
CREATE TABLE IF NOT EXISTS artists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Create albums table
CREATE TABLE IF NOT EXISTS albums (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    release_year INTEGER NOT NULL,
    artist_id BIGINT NOT NULL,
    CONSTRAINT fk_album_artist FOREIGN KEY (artist_id) REFERENCES artists(id) ON DELETE CASCADE
);

-- Create index on artist_id for better query performance
CREATE INDEX IF NOT EXISTS idx_albums_artist_id ON albums(artist_id);
