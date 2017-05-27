package com.TankWar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.Random;

public class Tank {
	private int x, y;
	public static final int XSPEED = 5, YSPEED = 5;
	public static final int WIDTH = 30, HEIGHT = 30;
	TankClient tc;
	private boolean bL = false, bU = false, bR = false, bD = false;
	enum Direction{L,LU,U,RU,R,RD,D,LD,STOP};
	private Direction dir = Direction.STOP;
	private Direction barreldir = Direction.D;
	private boolean good;
	private boolean live = true;
	private static Random r = new Random();
	private int step = r.nextInt(12) + 3;
	int oldX, oldY;
	private int life = 100;
	private BloodBar bb = new BloodBar();
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean isGood() {
		return good;
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
	}
	
	public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x,y,good);
		this.dir = dir;
		this.tc = tc;
	}
	
	public void draw (Graphics g) {
		if(!live) {
			if(!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		Color c = g.getColor();
		if(good) g.setColor(Color.red);
		else g.setColor(Color.blue);
		g.fillOval(x, y, WIDTH, HEIGHT);
		move();
		g.setColor(Color.BLACK);
		if(isGood()) {
			bb.draw(g);
		}
		switch(barreldir) {
		case L : 
			g.drawLine(x + Tank.WIDTH/2, y +Tank.HEIGHT/2, x, y +Tank.HEIGHT/2);
			break;
		case LU : 
			g.drawLine(x + Tank.WIDTH/2, y +Tank.HEIGHT/2, x, y);
			break;
		case U : 
			g.drawLine(x + Tank.WIDTH/2, y +Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
			break;
		case RU : 
			g.drawLine(x + Tank.WIDTH/2, y +Tank.HEIGHT/2, x + Tank.WIDTH, y);
			break;
		case R : 
			g.drawLine(x + Tank.WIDTH/2, y +Tank.HEIGHT/2, x + Tank.WIDTH, y +Tank.HEIGHT/2);
			break;
		case RD : 
			g.drawLine(x + Tank.WIDTH/2, y +Tank.HEIGHT/2, x + Tank.WIDTH, y +Tank.HEIGHT);
			break;
		case D : 
			g.drawLine(x + Tank.WIDTH/2, y +Tank.HEIGHT/2, x + Tank.WIDTH/2, y +Tank.HEIGHT);
			break;
		case LD : 
			g.drawLine(x + Tank.WIDTH/2, y +Tank.HEIGHT/2, x, y +Tank.HEIGHT);
			break;
		}
	}

	public void move() {
		oldX = x;
		oldY = y;
		switch(dir) {
		case L : 
			x -= XSPEED; 
			break;
		case LU : 
			x-= XSPEED; y -= YSPEED; 
			break;
		case U : 
			y -= YSPEED; 
			break;
		case RU : 
			x += XSPEED; y -= YSPEED; 
			break;
		case R : 
			x += XSPEED; 
			break;
		case RD : 
			x += XSPEED; y += YSPEED;
			break;
		case D : 
			y += YSPEED; 
			break;
		case LD : 
			x -= XSPEED; y += YSPEED; 
			break;
		case STOP : 
			break;
		}
		if(dir!= Direction.STOP) this.barreldir = this.dir;
		if(x < 0) {
			x = 0;
		}
		if(y < 30) {
			y=30;
		}
		if(x > TankClient.GAME_WIDTH - Tank.WIDTH) {
			x = TankClient.GAME_WIDTH - Tank.WIDTH;
		}
		if(y > TankClient.GAME_HEIGHT - Tank.HEIGHT) {
			y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		}
		if(!good) {
			if(step == 0){
				Direction[] dirs = Direction.values();
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;
			if(r.nextInt(40) > 38) {
				this.fire();
			}		
		}
	}
	
	public void stop() {
		x = oldX;
		y = oldY;
	}
	
	public void keypressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_F2:
			if(!live) {
				live = true;
				life = 100;
			}
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		}
		locateDirection();
	}
	
	public void keyreleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_CONTROL :
			fire();
			break;
		case KeyEvent.VK_LEFT :
			bL = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_A:
			superFire();
		}
		locateDirection();
	}
	
	public void locateDirection() {
		if(bL && !bD && !bR && !bU) dir = Direction.L;
		else if(bL && !bD && !bR && bU) dir = Direction.LU;
		else if(!bL && !bD && !bR && bU) dir = Direction.U;
		else if(!bL && !bD && bR && bU) dir = Direction.RU;
		else if(!bL && !bD && bR && !bU) dir = Direction.R;
		else if(!bL && bD && bR && !bU) dir = Direction.RD;
		else if(!bL && bD && !bR && !bU) dir = Direction.D;
		else if(bL && bD && !bR && !bU) dir = Direction.LD;
		else if(!bL && !bD && !bR && !bU) dir = Direction.STOP;				
	}
	
	public Missile fire() {
		if(!this.live) {
			return null;
		}
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, barreldir, this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public Missile fire(Direction dir) {
		if(!this.live) {
			return null;
		}
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, dir, this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	private void superFire() {
		Direction[] dirs = Direction.values();
		for(int i=0; i<8; i++) {
			fire(dirs[i]);
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x , y, WIDTH, HEIGHT);
	}
	
	public boolean hitWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.stop();
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(java.util.List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this != t) {
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
					this.stop();
					return true;
				}
			}		
		}
		return false;
	}
	
	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.red);
			g.drawRect(x, y-10, WIDTH, 10);
			int w = WIDTH * life/100;
			g.fillRect(x, y-10, w, 10);
			g.setColor(c);
		}
	}
	
	public boolean eat(Blood b) {
		if(this.live && this.getRect().intersects(b.getRect())) {
			this.life = 100;
			b.setLive(false);
			return true;
		}
		return false;
	}

}

