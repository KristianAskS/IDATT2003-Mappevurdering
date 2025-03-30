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