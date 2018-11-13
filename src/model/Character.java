package model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public abstract class Character extends Movable implements IDestructible {
	private int currentHP;
	private int maximumHP;
	private int defensePoints;
	private ArrayList<Effect> effects;
	private Color color;
	// a detruire
	private boolean toDestroy;

	public Character(int currentHP, int maximumHP, int defensePoints,
			GroundCollisionHandler groundCollisionHandler, int movingSpeedXMax,
			int movingSpeedYMax, Point position, Color c) {
		super(groundCollisionHandler, movingSpeedXMax, movingSpeedYMax,
				position);
		this.currentHP = currentHP;
		this.maximumHP = maximumHP;
		this.defensePoints = defensePoints;
		this.color = c;
		this.effects = new ArrayList<Effect>();
	}

	public int getCurrentHp() {
		return this.currentHP;
	}

	public Color getColor() {
		return color;
	}

	public boolean isAlive() {
		return currentHP >= 0;
	}

	public void applyDamages(int damages) {
		this.currentHP -= damages;
		if(currentHP<=0) {
			this.toDestroy = true;
		}
	}

	public void addEffect(Effect effect) {
		this.effects.add(effect);
	}

	@Override
	public void update() {
		applyEffects();
		super.update();
	}

	protected void applyEffects() {
		for (Effect e : effects) {
			e.apply();
		}
	}
	
	public int getEffectsSize(){
		return effects.size();
	}
	public Effect getEffect(int index) {
		return effects.get(index);
	}
	
	@Override
	public boolean isToDestroy() {
		return toDestroy;
	}
	
	public abstract void onCollision(Character character);
}