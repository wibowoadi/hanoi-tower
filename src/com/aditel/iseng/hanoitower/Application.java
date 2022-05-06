package com.aditel.iseng.hanoitower;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Application {
	public enum DiskStage {
		READY, UP, SLIDE, DOWN
	}

	private JFrame f;

	public static void main(String[] args) {
		Application app = new Application();
		app.init();
	}

	private void init() {

		f = new JFrame();// creating instance of JFrame
		f.setLocationRelativeTo(null);  
		f.setContentPane(new Content());
		f.setSize(600, 500);// 400 width and 500 height
		f.setLayout(null);// using no layout managers
		f.setVisible(true);// making the frame visible
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	class Content extends JPanel implements ActionListener {
		private static final long serialVersionUID = -3858307326417473647L;
		private static final Integer DEFAULT_DISK = 5;

		private JTextField diskNumText = new JTextField(String.valueOf(DEFAULT_DISK));
		private Integer diskNumber = DEFAULT_DISK;

		private Game game;

		private Map<Integer, Integer> towerNextSlots = new HashMap<Integer, Integer>();
		private Map<Integer, Integer> towerPositions = Map.of(0, 92, 1, 295, 2, 495);
		private Map<Integer, JPanel> disksMap = new HashMap<Integer, JPanel>();
		private DiskStage diskStage = DiskStage.READY;
		private Iterator<Step> stepIterator;
		private Step currentStep;
		private Integer stepNumber = 1;

		Content() {
			setupControl();
			setDoubleBuffered(true);
			setBackground(Color.blue.brighter());
//			setSize(300,200);
		}

		private void setupControl() {
			JLabel label = new JLabel("Number of Disks (1 - 9) : ");
			label.setBounds(20, 20, 180, 30);
			add(label);

			diskNumText.setHorizontalAlignment(JTextField.CENTER);
			diskNumText.setBounds(200, 20, 60, 30);
			add(diskNumText);

			JButton button = new JButton("Start");
			button.setBounds(280, 20, 120, 30);
			add(button);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					start();
				}
			});

		}

		private void start() {
			diskNumber = Integer.parseInt(diskNumText.getText());
			setupDisk();
			setupTower();
			repaint();


			stepNumber = 1;
			
			game = new Game(diskNumber);
			game.solve();
			stepIterator = game.getSteps().iterator();

			Timer timer = new Timer(15, this);
			timer.setCoalesce(false);
			timer.setInitialDelay(500);
			timer.start();

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (diskStage == DiskStage.READY) {
				if (stepIterator.hasNext()) {
					currentStep = stepIterator.next();

					System.out.println("Step %s tower %s -> tower %s".formatted(stepNumber++, currentStep.fromTower(),
							currentStep.toTower()));
					diskStage = DiskStage.UP;
				} else {
					return;
				}
			}

			JPanel disk = disksMap.get(currentStep.diskId());
			int x = (int) disk.getBounds().getX();
			int y = (int) disk.getBounds().getY();

			if (diskStage == DiskStage.UP && y > 150) {
				y = y - 5;

			}

			if (diskStage == DiskStage.UP && y <= 150) {
				diskStage = DiskStage.SLIDE;
			}

			Integer towerPosition = towerPositions.get(currentStep.toTower());
			Integer diskXPosition = (int) (x + disk.getBounds().getWidth() / 2);

			int op = 0;
			if (diskStage == DiskStage.SLIDE && diskXPosition != towerPosition) {
				op = towerPosition > diskXPosition ? 1 : -1;
				x = x + 5 * op;
			}

			boolean timeToDown = Math.abs(diskXPosition - towerPosition) <= 5;
			if (diskStage == DiskStage.SLIDE && timeToDown) {
				diskStage = DiskStage.DOWN;
			}

			if (diskStage == DiskStage.DOWN && y < towerNextSlots.get(currentStep.toTower())) {

				y = y + 5;
			}

			if (diskStage == DiskStage.DOWN && y >= towerNextSlots.get(currentStep.toTower())) {
				int oldToTowerPosition = towerNextSlots.get(currentStep.toTower());
				int oldFromTowerPosition = towerNextSlots.get(currentStep.fromTower());
				towerNextSlots.put(currentStep.toTower(), oldToTowerPosition - 20);
				towerNextSlots.put(currentStep.fromTower(), oldFromTowerPosition + 20);
				diskStage = DiskStage.READY;
			}

			int width = (int) disk.getBounds().getWidth();
			int height = (int) disk.getBounds().getHeight();
			disk.setBounds(x, y, width, height);
			repaint();
		}

		private void setupTower() {
			JPanel floor = new JPanel();
			floor.setBounds(40, 400, 520, 10);
			floor.setBackground(Color.decode("#231709"));
			add(floor);

			JPanel tower0 = new JPanel();
			tower0.setBounds(90, 210, 10, 190);
			tower0.setBackground(Color.decode("#231709"));
			add(tower0);

			JPanel tower1 = new JPanel();
			tower1.setBounds(290, 210, 10, 190);
			tower1.setBackground(Color.decode("#231709"));
			add(tower1);

			JPanel tower2 = new JPanel();
			tower2.setBounds(490, 210, 10, 190);
			tower2.setBackground(Color.decode("#231709"));
			add(tower2);

		}

		private void setupDisk() {
			disksMap.forEach((k, j) -> {
				remove(j);
			});
			disksMap.clear();
			towerNextSlots.clear();

			Color color = Color.red.darker().darker();
			for (int i = 0; i < diskNumber; i++) {
				JPanel disk = new JPanel();
				disk.setBounds(45 + i * 5, 380 - i * 20, 100 - i * 10, 10);

				disk.setBackground(color);
				add(disk);

				disksMap.put(i, disk);
				color = brighten(color, 0.2);
			}

			towerNextSlots.put(0, 380 - 20 * diskNumber);
			towerNextSlots.put(1, 380);
			towerNextSlots.put(2, 380);

		}

		public static Color brighten(Color color, double fraction) {

			int red = (int) Math.round(Math.min(255, color.getRed() + 255 * fraction));
			int green = (int) Math.round(Math.min(255, color.getGreen() + 255 * fraction));
			int blue = (int) Math.round(Math.min(255, color.getBlue() + 255 * fraction));

			int alpha = color.getAlpha();

			return new Color(red, green, blue, alpha);

		}

	}

}
