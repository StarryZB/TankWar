package com.TankWar;
import java.awt.*;
import java.util.List;

public class Missile {
	int x, y;
	public static final int XSPEED = 10, YSPEED = 10;
	public static final int WIDTH = 10, HEIGHT = 10;
	Tank.Direction dir;
	private boolean live = true;
	private TankClient tc;
	private boolean good;
	
	public boolean isLive() {
		return live;
	}

	public Missile(int x, int y, Tank.Direction dir) {
		super();
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, boolean good, Tank.Direction dir, TankClient tc) {
		this(x,y,dir);
		this.good = good;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!live){
			return;
		}
		Color c = g.getColor();
		if(!good) {
			g.setColor(Color.yellow);
		}
		else g.setColor(Color.black);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		move();
	}
	
	public void move() {
		switch(dir) {
		case L : x -= XSPEED; break;
		case LU : x -= XSPEED; y -= YSPEED; break;
		case U : y -= YSPEED; break;
		case RU : x += XSPEED; y -= YSPEED; break;
		case R : x += XSPEED; break;
		case RD : x += XSPEED; y += YSPEED; break;
		case D : y += YSPEED; break;
		case LD : x -= XSPEED; y += YSPEED; break;
		case STOP : break;
		}
		if( x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			live = false;
			tc.missiles.remove(this);
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean hitTank(Tank t) {
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
			if(t.isGood()) {
				t.setLife(t.getLife() - 10);
				if(t.getLife() <= 0) {
					t.setLive(false);
				}
			} else {
					t.setLive(false);
				}
						
			this.live = false;
			if(!live) {
				tc.missiles.remove(this);
			}
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hittanks(List<Tank> tanks) {
		for(int i=0;i<tanks.size();i++) {
			if(hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hitwall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			tc.missiles.remove(this);
			return true;
		}
		return false;
	}
}