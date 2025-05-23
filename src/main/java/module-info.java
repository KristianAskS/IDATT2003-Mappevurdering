module edu.ntnu.bidata.idatt {
  requires javafx.controls;
  requires com.google.gson;
  requires java.logging;
  requires java.desktop;
  requires org.jetbrains.annotations;
  opens edu.ntnu.bidata.idatt.utils.io to com.google.gson;
  exports edu.ntnu.bidata.idatt;
  exports edu.ntnu.bidata.idatt.view.components;
  exports edu.ntnu.bidata.idatt.view.scenes;
  opens edu.ntnu.bidata.idatt.model.entity to com.google.gson;
  exports edu.ntnu.bidata.idatt.controller;
  exports edu.ntnu.bidata.idatt.model.entity;
}