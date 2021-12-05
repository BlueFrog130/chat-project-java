# Chat Project
### Adam Grady & Yanmin Lynch

Must use latest version of Java, which is [Java 17](https://openjdk.java.net/projects/jdk/17/) at the time of creating this project.

[VS Code](https://code.visualstudio.com/) with the Java extension pack is preconfigured to run this app. Although the `JavaFX` library must be extracted to `lib/javafx/` folder

Project directory breakdown

```bash
├───ChatProject -- Server
└───ChatProjectClient -- Client
    ├───lib -- external libraries
    └───src
        ├───css
        └───fxml
```

## Chat Server (_ChatProject_)

Project housing the server for the chat. Runs by default on port 8000, can be configured via passed in args.

Run using `java src/App.java`

## Chat Client (_ChatProjectClient_)

Basic JavaFX project using FXML, which is a way to define GUI using structured language.

To run, first the latest version of [JavaFX](https://gluonhq.com/products/javafx/) must be downloaded and extracted into the `lib/javafx` folder.

Then run using `java --enable-preview --module-path lib/javafx/lib --add-modules javafx.controls,javafx.fxml src/App.java`
