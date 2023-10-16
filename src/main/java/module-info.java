module flightsfx.model.flightsfx {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens flightsfx.model to javafx.fxml;
    exports flightsfx.model;
}