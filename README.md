# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Updated Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIcDcuj3ZfF5vD6L9sgwr5iWw63O+nxPF+SwfgC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm6ZIUgatJvqMJpEuGFoctyvIGoKwowKK4qutKSaXvB5RGtojrOgS+EsuUnrekGAZBiGLHmpGHLRjAsbBtxOGdoBaZoTy2a5pgymdsWabARW46tl806zs2Bm-jpnbZD2MD9oOvSviOowLpOfTGY2pkTn0S6cKu3h+IEXgoOge4Hr4zDHukmSYFZF5FNQ17SAAoruiX1IlzQtA+qhPt007QEgABe8SJOUAA8blzvkf5ssp5R5VAhXFWgZUVegVVaXBTryjASH2GFqGhb6GEYthcq4XxFEETA5JgHJol1u5aDkUybpUVyPIxmJ2hCmErVoKGk2sZ14IwNO8ZjRq-GkkYKDcJkc1ndoy1mhGhSWtIt0UoYcn0btQYmftilsWcQIlmhYXqQgeYwR2wM6VcMAHBZwMxWAfYDkOiNmD5nh+RukK2ru0IwAA4qOrIRae0XnswnXXiTqUZfYo65UG+VFQQJUwOV-2LVVyMATDab1Y1nPNdze3tULbI4Yh0KodCw1YTxiYTStAnTRSD283Oz2UVJ628j920MXtB3q5Jx3dY98gq+NMhXe6PXQmToyqLCeurQbNG2q7KDnfBSlC+UROxJD0OgyCdO6VMzNu+MlT9HHKAAJLSAnACMvYAMwACxPCemQGhWYEwN0fQ6AgoANsXoHVrHo4AHKjv8MCNEjV5R4UqPo3ZPQN-H5QVEno5p5nOf51Mhf6qR9z1+XlfV7XTml30yfN6Mrft9jK64+ugTYD4UDYNw8C6pkpOjikkVnjktPsSUQ+1A0TMs8EbMNRzT4tTrbUvuvdcvLVWTMHU6H9Rbfwlr-NA+R-5N0AdBSOMsxqCXPigWEcA0FKyxHbS6h1rozW1gtXW5sXprR9rJLa8gdpgOIegUh+srYIVofJW2QM1YvVQV6TIftYTJ3Eo7NaMk-YBy6rhEBkdyiYO4SgcOmlpZw07gjNeo907lCznnLGyNTg9xshjXoA9U5qJgBo3OWNly+X3gESwt0kLJBgAAKQgDyS+oxAiLxAA2amd9kHxTTNUSkd4WjJ1Zn6dmTUf50JgS+E+wAbFQDgBAJCUAVgAHUWApzSi0AAQruBQcAADSMxy7JzHuoieWjO41VASLL+XMeZRNgfZWJ8TEnJLSRkrJuT8lFOmCU1R49NGINTL4sR5QABWzi0CwicWpFAaIRq4Lwvgp2hCRJ7U9qxN61ENqULjCbP6USGFeyYXVKhwBjlbPZIbX2o5frTVUZcy2D8Tr8IUhdU5MAQCpBQJ4+oXphhtOgHwu52hZgtMoECqAo1A7LItk7PwWgeGjhBaMARKy1qUmwEiwwfsaGlOkEsiRqZyizLQHIjq0cEYdz8ZZGmvdMYHB3pY-yAQvBxK7F6WAwBsAn0IE1a+VNUajISslVK6VWjGGAYLSRMB5FIM+SAbgeAMHKqzPMzCOD2EOxWYJNVdFtAeyea9a5H07qGAAGbeGGHaAU7zYUcPDHqrlxt5BGokia96n0L5WogDa11FztUeudXgERhrNmSW2aUM1X0YC+ptWGthHziXAikWqilCiqWlkqbSlG9K9F93MT5IAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
