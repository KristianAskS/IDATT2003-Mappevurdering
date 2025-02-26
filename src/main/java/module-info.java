module com.ntnu.idatt2003.idatt2003 {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;
  
  requires org.controlsfx.controls;
  requires net.synedra.validatorfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
  requires eu.hansolo.tilesfx;
  requires com.almasb.fxgl.all;
  
  opens com.ntnu.idatt2003 to javafx.fxml;
  exports com.ntnu.idatt2003;
  exports com.ntnu.idatt2003.view;
  opens com.ntnu.idatt2003.view to javafx.fxml;
}