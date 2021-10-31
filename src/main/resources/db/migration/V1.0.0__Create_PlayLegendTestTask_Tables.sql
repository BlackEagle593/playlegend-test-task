-- Create user name table
CREATE TABLE user_name
(
    unique_id   UUID PRIMARY KEY,
    player_name VARCHAR(16) NOT NULL
);

-- Create player name index for user name table
CREATE UNIQUE INDEX user_player_name ON user_name (player_name);

-- Create ban table
CREATE TABLE ban
(
    unique_id     UUID PRIMARY KEY,
    end_timestamp BIGINT NOT NULL,
    cause         TEXT   NOT NULL
)
