package neuro.classification;

import javafx.stage.Stage;
import neuro.classification.controller.MainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@EnableAutoConfiguration
@ComponentScan("neuro.classification")
public class Application extends AbstractJavaFxApplicationSupport {

	@Autowired
	private MainController mainController;

	public static void main(final String[] args) {
		launchApp(Application.class, args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		primaryStage.setScene(mainController.buildMainScene());
		primaryStage.show();
	}
}
