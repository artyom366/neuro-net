package neuro.classification.component;

import javafx.scene.control.Label;

public class LabelFactory {

    public static Label getLabel(final float x, final float y, final float height, final float width, final String caption) {
        final Label label = new Label(caption);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setMinWidth(width);
        label.setMinHeight(height);
        return label;
    }
}
