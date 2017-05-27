package com.TankWar;
import java.awt.*;

public class Blood {
	int x, y, w, h;
	TankClient tc;
	int step = 0;
	private boolean live = true;
	


	public int[][] pos = {
			{350,300},{351,300},{352,300},{353,300},{354,300},{354,301},{353,301},{352,301},{351,301}
	};
	
	public boolean isLive() {
		return live;
	}
	
	public void setLive(boolean live) {
		this.live = live;
	}
	
	public Blood() {
		x = pos[0][0];
		y = pos[0][1];
		w = h = 15;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.magenta);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		move();
	}
	
	private void move() {
		step++;		
		if(step == pos.length) {
			step = 0;	
		}	
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
	
}
