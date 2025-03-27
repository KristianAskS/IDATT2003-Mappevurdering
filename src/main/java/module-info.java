module com.ntnu.idatt {
  requires javafx.controls;
  requires com.google.gson;
  requires java.logging;
  opens edu.ntnu.bidata.idatt.model to com.google.gson;
  exports edu.ntnu.bidata.idatt;
  exports edu.ntnu.bidata.idatt.view;
}