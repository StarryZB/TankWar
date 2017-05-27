package com.TankWar;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	Tank mytank = new Tank(50, 50, true, Tank.Direction.STOP, this);	
	Missile mymissile = null;
	Explode e = new Explode(70, 70, this);
	Wall w1 = new Wall(100, 200, 20, 150, this);
	Wall w2 = new Wall(300, 100, 300, 20, this);
	Blood b = new Blood();
	int x = 50; int y = 50;
	Image offScreenImage = null;
	List <Missile> missiles = new ArrayList<Missile>();
	List <Explode> explodes = new ArrayList<Explode>();
	List <Tank> tanks = new ArrayList<Tank>();

	
	public void paint(Graphics g) {		
		for (int i=0; i<missiles.size();i++) {
			Missile m = missiles.get(i);
			m.hittanks(tanks);
			m.hitTank(mytank);
			m.hitwall(w1);
			m.hitwall(w2);
			m.draw(g);
		}
		for (int i=0; i<explodes.size();i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		for (int i=0; i<tanks.size();i++) {
			Tank t = tanks.get(i);
			t.draw(g);
			t.hitWall(w1);
			t.hitWall(w2);
			t.hitTanks(tanks);
		}
		if(tanks.size() <= 0) {
			for(int i=0; i<5; i++) {
				tanks.add(new Tank(100+40*(i+1), 1000, false, Tank.Direction.D, this));
			}
		}
		mytank.draw(g);
		mytank.hitWall(w1);
		mytank.hitWall(w2);
		mytank.eat(b);
		w1.draw(g);
		w2.draw(g);
		b.draw(g);
		if(!b.isLive()) {
			new Thread(new BloodThread()).start();
			}
	}
	
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics goffScreenImage = offScreenImage.getGraphics();
		Color c = goffScreenImage.getColor();
		goffScreenImage.setColor(Color.green);
		goffScreenImage.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		paint(goffScreenImage);
		g.drawImage(offScreenImage, 0, 0, null);
		g.drawString("missile count:" + missiles.size(), 10, 60);
		g.drawString("explode count:" + explodes.size(), 10, 80);
		g.drawString("tank    count:" + tanks.size(), 10, 100);
		g.drawString("tank     life:" + mytank.getLife(), 10, 120);
	}

	public void lauchFrame() {
		this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		this.addKeyListener(new KeyMonitor());
		for(int i=0; i<10; i++) {
			tanks.add(new Tank(100+40*(i+1), 1000, false, Tank.Direction.D, this));
		}
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		new Thread(new PaintThread()).start();

	}	

	public static void main(String[] atges){
	TankClient tc = new TankClient();
	tc.lauchFrame();
	}
	
	private class PaintThread implements Runnable {
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	private class BloodThread implements Runnable {
		public void run() {
			boolean flag = true;
			while(flag) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally{
						b.setLive(true);
						flag = false;
					}
					
				//Thread.sleep(10000);
			}	
		}
	}
	
	private class KeyMonitor extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			mytank.keyreleased(e);
		}

		public void keyPressed(KeyEvent e) {
			mytank.keypressed(e);				
			}
	}		
}