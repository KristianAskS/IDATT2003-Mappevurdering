# Boardgame Project

## 📌 Om prosjektet

Dette prosjektet er en del av **IDATT2003 Programmering 2** ved NTNU. Målet er å utvikle diverse digitale
brettspill i Java med JavaFX. Det første spillet som inngår i MVP som skal utvikles er et enkelt stigespill. 

### 🎯 Funksjoner

- 🎲 **Terningkast**: Spillere beveger seg ved å kaste terninger.
- 🔼 **Stiger og spesialfelt**: Flytt opp eller ned på brettet basert på feltenes regler.
- 🖥️ **Grafisk brukergrensesnitt**: JavaFX for et interaktivt og brukervennlig spill.
- ✅ **Enhetstesting**: JUnit 5 for å sikre stabilitet.

---

## 📥 Installasjon

Følg disse stegene for å sette opp prosjektet lokalt.

### **1. Klon prosjektet fra GitHub/GitLab**

```sh
git clone https://github.com/ditt-repo/boardgame.git
cd boardgame
```

### **2. Bygg og kjør prosjektet med Maven**

```sh
mvn clean install
mvn javafx:run
```

**Krav:**

- Java 21 (LTS)
- Maven 3.8+

---

## 🕹️ Brukerveiledning

1. Start spillet ved å kjøre `App.java` eller `mvn javafx:run`.
2. Velg antall spillere og navn.
3. Hver spiller kaster terninger og flytter brikken sin.
4. Spillet fortsetter til en spiller når det siste feltet.

---

## 🛠️ Teknologi og avhengigheter

- **Java 21** - Programmeringsspråk
- **JavaFX 23.0.1** - GUI
- **JUnit 5.11.4** - Enhetstesting
- **Maven** - Byggeverktøy
- **Git** - Versjonskontroll

---

## 🧪 Testing

Kjør enhetstester med Maven:

```sh
mvn test
```

---

## 📖 Videre utvikling

- 🔹 Implementere flere spilltyper (f.eks. Monopol-lignende varianter).
- 🔹 Forbedret AI for datastyrte spillere.
- 🔹 Online multiplayer.

## 📝 Bidrag

1. Fork repoet.
2. Lag en ny branch (`git checkout -b feature-navn`).
3. Gjør endringer og commit (`git commit -m "La til ny funksjon"`).
4. Push til GitHub og lag en pull request.

Vi setter pris på alle bidrag! 🚀

## 🌲 Branch-konvensjon

Vi følger en standard konvensjon for branch-navn insiprert av Angular. Alle nye branches opprettes
fra `dev`.

- **Hotfix-branches:**
  ```
  hotfix(<scope>)/<beskrivelse>
  ```
  _Example:_
  ```
  hotfix(parser)/empty-string-handling
  ```

- **Bugfix-branches:**
  ```
  fix(<scope>)/<beskrivelse>
  ```
  _Example:_
  ```
  fix(parser)/end-of-file-handling
  ```

- **Refactor-branches:**
  ```
  refactor(<scope>)/<beskrivelse>
  ```
  _Example:_
  ```
  refactor(parser)/refactor-null-values
  ```

- **Dokumentasjonsendringer:**
  ```
  docs(<scope>)/<beskrivelse>
  ```
  _Example:_
  ```
  docs(readme)/update-readme
  ```

- **Feature-branches:**
  ```
  feat(<scope>)/<beskrivelse>
  ```
  _Example:_
  ```
  feat(parser)/handle-null-values
  ```

## 📄 Commit-meldinger

Vi følger en standardkonvensjon for commit-meldinger.
Du kan lese mer om det
her: [https://www.conventionalcommits.org/en/v1.0.0/](https://www.conventionalcommits.org/en/v1.0.0/)