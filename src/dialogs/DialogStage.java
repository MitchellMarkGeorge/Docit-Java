package dialogs;

import javafx.stage.Stage;

public abstract class DialogStage extends Stage {// better name
    

    public void showStage() {
        this.show();
    }

    public void closeStage() {
        this.close();
    }
}
