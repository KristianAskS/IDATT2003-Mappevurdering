
# A Board game Application


## 📌 About the Project


This project is part of **IDATT2003 Programming 2** at NTNU. The goal is to develop various digital board games in Java
using JavaFX. The first game included in the MVP to be developed is a simple snakes-and-ladders-style game.


### 🎯 Features


- 🎲 **Dice Rolling**: Players move by rolling dice.
- 🔼 **Ladders and Special Tiles**: Move up or down the board based on tile rules.
- 🖥️ **Graphical User Interface**: JavaFX provides an interactive and user-friendly experience.
- ✅ **Unit Testing**: JUnit 5 ensures stability.


---


## 📥 Installation


Follow these steps to set up the project locally.


### **1. Clone the project**


```sh
git clone https://github.com/ditt-repo/boardgame.git
cd boardgame
```


### **2. Build and run the project with Maven**


```sh
mvn clean install
mvn javafx:run
```


**Requirements:**


- Java 21 (LTS)
- Maven 3.8+


---


## 🕹️ User guide


1. Start the game by running `mvn javafx:run`.
2. Choose a game to play
3. Choose the number of players and enter names and tokens.
4. Each player rolls the dice and moves their piece.
5. The game continues until a player reaches the final tile.


---


## 🛠️ Teknologi og avhengigheter


- **Java 21** - Programming language
- **JavaFX 23.0.1** - GUI framework
- **JUnit 5.11.4** - Unit testing
- **Maven** - Build tool
- **Git** - Version control


---


## 🧪 Testing


Run unit tests with Maven:


```sh
mvn test
```


---


## Design


The design mockups and prototypes for this project are available on Figma:


👉 [View Figma Project](https://www.figma.com/design/ZfKK6YWx7Wqll1PPB2EkfK/MVP?node-id=0-1&t=MbkANIOIG36zA1f5-1)


## 📖 Further Development


- 🔹 Implement additional game types (e.g., Monopoly-style games).
- 🔹 Improved AI (dummies/robots/NPCs) for computer-controlled players.
- 🔹 Online multiplayer support.


## 📝 Bidrag


1. Fork the repository.
2. Create a new branch (`git checkout -b feature-name`).
3. Make your changes and commit (`git commit -m "La til ny funksjon"`).
4. Push to GitHub and create a pull request.

