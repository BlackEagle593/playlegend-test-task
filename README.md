# PlayLegend Test Task

The test task for the playlegend application.

## Description

The test task is a simple ban system.
Players can be banned for a given period of time with a cause via a command.
Banned players can be unbanned via command.

### Test Task Functional Requirements

1. Players can be banned or unbanned via a command.
2. The ban has a duration or is permanent.
3. The ban has a cause.
4. When a banned player tries to join he will be shown the reason and the remaining time of the ban.
5. The bans are stored in a relation database.

### Test Task Nonfunctional requirements

1. The code is easy to read and understandable
2. The Java conventions are adhered to.
3. The plugin is high-performance and is able to edit many players at once without any problems.

## Usage

- Create docker network
```
docker network create playlegend-test-task
```

- Start a postgres database container.
```
docker run --name postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=123456 -e POSTGRES_DB=minecraft --network playlegend-test-task -p 5432:5432 -d postgres
```

- Pull and start the minecraft server container with the playlegend test task plugin.
```
docker pull ghcr.io/blackeagleef/playlegend-test-task:latest
docker run --name playlegend-test-task --network playlegend-test-task -p 25565:25565 ghcr.io/blackeagleef/playlegend-test-task:latest
```

### Ban command

```
/ban <player|uuid> <duration> <cause>
```

- `player|uuid`: The name or the uniqueId of the player. The name can only be used if the player has been online at least once.
- `duration`: Without a suffix, the duration is given in seconds. The suffixes are as follows:
  - `s`: Seconds
  - `m`: Minutes
  - `h`: Hours
  - `D`: Days
  - `M`: Months
  - `Y`: Years
- `cause`: The cause of the ban can be of any length.

### Unban command

```
/unban <player|uuid>
```

- `player|uuid`: The name or the uniqueId of the player. The name can only be used if the player has been online at least once.
