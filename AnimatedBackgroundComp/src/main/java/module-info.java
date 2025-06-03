module org.example.animatedbackgroundcomp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;



    opens org.example.animatedbackgroundcomp to javafx.fxml;
    exports org.example.animatedbackgroundcomp;
}