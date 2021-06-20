# Mancala Game
The Mancala Game is the so old game which is played by 2 players, 1 board and some stones.

#### Restful Service uses following Technologies:

* Spring Boot 2.5
* Swagger (OpenAPI v3)

#### Front-end
* Spring Boot (Thymeleaf)
* Bootstrap
* JQuery

## Run
If you would like to build in your local, you can use below method
```  
mvn clean install  
```  

But because of multi-stage build, directly by running below command, application will start, 
```  
docker-compose up 
```   
PS: If you use newer version of Docker, "Docker Compose is now in the Docker CLI, try `docker compose up`"

## Usage
### Swagger
```  
http://localhost:8080/documentation  
```   
### Back-end
This service has two endpoints:
```  
GET: http://localhost:8080/start (parameter: gameId)
```
returns new game board if gameId is null or not exist, otherwise returns existing game board.
```  
GET: http://localhost:8080/move (parameter: movement object)
```  
returns calculated game board if movement is proper

### Front-end
From your favourite browser, you can access below URI and enjoy playing this game.
```  
http://localhost:8080  
```
![image](https://user-images.githubusercontent.com/2255525/122678946-75496580-d1f1-11eb-9b02-478d086606bf.png)


## Technical
When I decide to implement this game, firstly I thought that to save games, I should use a database or file or an in-memory cache, etc. But to decrease complexity and dependencies, I have decided to keep game data in a HashMap.

The max capacity of a HashMap is 2^30 so for a simple game application, this will be enough. Sure, in every deployment or restart, existing games will be gone. But for this game, we can accept this problem.

For the front-end, I prefer to use Thymeleaf because it has so fast implementation. FE and BE projects should be independent but in this case, I discard this because of the same above reason, less complexity. For FE actions, I prefer jQuery, because it is easy although so old and I like it :)




