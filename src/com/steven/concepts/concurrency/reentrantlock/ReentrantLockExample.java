package com.steven.concepts.concurrency.reentrantlock;

import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * stevmc created on 3/1/20
 */
public class ReentrantLockExample extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Cryptocurrency Prices");
		GridPane grid = createGrid();

		Map<String, Label> cryptoLabels = createCryptoPriceLabels();

		addLabelsToGrid(cryptoLabels, grid);

		double width = 300;
		double height = 250;
		Rectangle background = createBackgroundRectangleWithAnimation(width, height);

		StackPane root = new StackPane();
		root.getChildren().add(background);
		root.getChildren().add(grid);

		primaryStage.setScene(new Scene(root, width, height));

		PricesContainer pricesContainer = new PricesContainer();
		PricesUpdater pricesUpdater = new PricesUpdater(pricesContainer);

		AnimationTimer animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				// if you change this to the lock method instead of the try lock method the application becomes unresponsive due to fighting threads.
				if (pricesContainer.getLock().tryLock()) {

					try {
						Label bitcoinLabel = cryptoLabels.get("BTC");
						bitcoinLabel.setText(String.valueOf(pricesContainer.getBitcoinPrice()));

						Label etherLabel = cryptoLabels.get("ETH");
						etherLabel.setText(String.valueOf(pricesContainer.getEtherPrice()));

						Label liteCoinLabel = cryptoLabels.get("LTC");
						liteCoinLabel.setText(String.valueOf(pricesContainer.getLitecoinPrice()));

						Label bitcoinCashLabel = cryptoLabels.get("BCH");
						bitcoinCashLabel.setText(String.valueOf(pricesContainer.getBitcoinCashPrice()));

						Label rippleLabel = cryptoLabels.get("XRP");
						rippleLabel.setText(String.valueOf(pricesContainer.getRipplePrice()));

					} finally {
						pricesContainer.getLock().unlock();
					}
				}
			}
		};

		animationTimer.start();
		pricesUpdater.start();

		primaryStage.show();
	}

	private static class PricesContainer {
		private Lock lock = new ReentrantLock();

		private double bitcoinPrice;
		private double etherPrice;
		private double litecoinPrice;
		private double bitcoinCashPrice;
		private double ripplePrice;

		public Lock getLock() {
			return lock;
		}

		public void setLock(Lock lock) {
			this.lock = lock;
		}

		public double getBitcoinPrice() {
			return bitcoinPrice;
		}

		public void setBitcoinPrice(double bitcoinPrice) {
			this.bitcoinPrice = bitcoinPrice;
		}

		public double getEtherPrice() {
			return etherPrice;
		}

		public void setEtherPrice(double etherPrice) {
			this.etherPrice = etherPrice;
		}

		public double getLitecoinPrice() {
			return litecoinPrice;
		}

		public void setLitecoinPrice(double litecoinPrice) {
			this.litecoinPrice = litecoinPrice;
		}

		public double getBitcoinCashPrice() {
			return bitcoinCashPrice;
		}

		public void setBitcoinCashPrice(double bitcoinCashPrice) {
			this.bitcoinCashPrice = bitcoinCashPrice;
		}

		public double getRipplePrice() {
			return ripplePrice;
		}

		public void setRipplePrice(double ripplePrice) {
			this.ripplePrice = ripplePrice;
		}
	}

	private static class PricesUpdater extends Thread {
		private final PricesContainer pricesContainer;
		private Random random = new Random();

		public PricesUpdater(PricesContainer pricesContainer) {
			this.pricesContainer = pricesContainer;
		}

		@Override
		public void run() {
			while (true) {
				pricesContainer.getLock().lock();
				try {
					// simulate network call or complex computation with thread sleep
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					pricesContainer.setBitcoinPrice(random.nextInt(20000));
					pricesContainer.setEtherPrice(random.nextInt(20000));
					pricesContainer.setLitecoinPrice(random.nextInt(20000));
					pricesContainer.setBitcoinCashPrice(random.nextInt(20000));
					pricesContainer.setRipplePrice(random.nextInt(20000));
				} finally {
					pricesContainer.getLock().unlock();
				}

				// simulate network call or complex computation with thread sleep
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private GridPane createGrid() {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setAlignment(Pos.CENTER);
		return grid;
	}

	private Map<String, Label> createCryptoPriceLabels() {
		Label bitcoinPrice = new Label("0");
		bitcoinPrice.setId("BTC");

		Label eitherPrice = new Label("0");
		eitherPrice.setId("ETH");

		Label liteCoinPrice = new Label("0");
		liteCoinPrice.setId("LTC");

		Label bitcoinCashPrice = new Label("0");
		bitcoinCashPrice.setId("BCH");

		Label ripplePrice = new Label("0");
		ripplePrice.setId("XRP");

		Map<String, Label> map = new HashMap<>();
		map.put("BTC", bitcoinPrice);
		map.put("ETH", eitherPrice);
		map.put("LTC", liteCoinPrice);
		map.put("BCH", bitcoinCashPrice);
		map.put("XRP", ripplePrice);

		return map;
	}

	private void addLabelsToGrid(Map<String, Label> cryptoLabels, GridPane grid) {
		int row = 0;
		for (Map.Entry<String, Label> entry : cryptoLabels.entrySet()) {

			String cryptoName = entry.getKey();
			Label label = new Label(cryptoName);
			label.setTextFill(Color.BLUE);
			label.setOnMousePressed(event -> label.setTextFill(Color.RED));
			label.setOnMouseReleased(event -> label.setTextFill(Color.BLUE));

			grid.add(label, 0, row);
			grid.add(entry.getValue(), 1, row);
			row++;
		}
	}

	private Rectangle createBackgroundRectangleWithAnimation(double width, double height) {
		Rectangle background = new Rectangle(width, height);
		FillTransition fill = new FillTransition(Duration.millis(1000), background, Color.LIGHTGREEN, Color.LIGHTBLUE);
		fill.setCycleCount(Timeline.INDEFINITE);
		fill.setAutoReverse(true);
		fill.play();
		return background;

	}
}
