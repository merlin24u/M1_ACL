package model.painter;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;


import engine.Game;
import engine.GamePainter;
import model.GameState;
import model.Map;
import model.PacmanGame;
import model.Time;
import model.item.Item;
import model.movable.character.Character;
import model.movable.character.Pacman;
import model.movable.character.monster.Monster;
import model.movable.collision.ECollisionType;
import model.onmoveover.OnMoveOver;
import texture.TextureFactory;

/**
 * @author Horatiu Cirstea, Vincent Thomas
 * 
 *         afficheur graphique pour le game
 * 
 */
public class PacmanPainter implements GamePainter {

	/**
	 * la taille des cases
	 */
	protected int width;
	protected int height;
	protected static int TILE_WIDTH = 50;
	protected static int TILE_HEIGHT = 50;

	protected static Color BAR_COLOR =Color.BLACK;
	protected static int BAR_HEIGHT =30;
	protected static int BAR_SPACING_WIDTH = 18;
	protected static Color BAR_BACKGROUND_COLOR = Color.GREEN;
	protected static Font BAR_FONT = new Font("Verdana",Font.PLAIN,18);
	private PacmanGame game;

	private CollisionPainterResponsability collisionPainter;

	/**
	 * appelle constructeur parent
	 * 
	 * @param game
	 *            le jeutest a afficher
	 */
	public PacmanPainter(PacmanGame game, int width, int height) {
		this.game = game;
		this.width = width;
		this.height = height;
		WallCollisionPainterResponsability wallCollisionPainter = new WallCollisionPainterResponsability(ECollisionType.WALL);
		collisionPainter = new GroundCollisionPainterResponsability(ECollisionType.NONE);
		collisionPainter.setSuccessor(wallCollisionPainter);
	}

	private void drawBar(BufferedImage im) { 
		Pacman player = game.getPlayer();
		Graphics2D crayon = (Graphics2D) im.getGraphics();
		crayon.setFont(BAR_FONT);
		crayon.setColor(BAR_BACKGROUND_COLOR);
		crayon.fillRect(0,0,getWidth(), BAR_HEIGHT);
		crayon.setColor(BAR_COLOR);
		int healthBarWidth = getWidth()/4;
		int healthBarHeight = BAR_HEIGHT /2;
		int nextMarginLeft = BAR_SPACING_WIDTH;
		crayon.drawString("Money : "+player.getMoneyAmount(), nextMarginLeft, BAR_HEIGHT-(BAR_HEIGHT-BAR_FONT.getSize())/2);
		nextMarginLeft += crayon.getFontMetrics().stringWidth("Money : "+player.getMoneyAmount()) + BAR_SPACING_WIDTH;
		crayon.drawString("Inventory : ", nextMarginLeft, BAR_HEIGHT-(BAR_HEIGHT-BAR_FONT.getSize())/2);
		nextMarginLeft += crayon.getFontMetrics().stringWidth("Inventory : ");
		for(Item i : player.getItems()) {
			if(i.getTexture()!=null) {
				try {
					BufferedImage img = (BufferedImage) TextureFactory.getInstance().get(i.getTexture());
					int itemHeight = (int)(BAR_HEIGHT*0.75);
					int itemMarginTop = (BAR_HEIGHT - itemHeight)/2;
					int itemScale = img.getHeight()/itemHeight;
					int itemWidth = img.getWidth()/itemScale;
					crayon.drawImage(img, nextMarginLeft, itemMarginTop, itemWidth, itemHeight, null);
					nextMarginLeft += itemWidth+BAR_SPACING_WIDTH;
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		crayon.setColor(Color.BLACK);
		int nextMarginRight = BAR_SPACING_WIDTH;
		crayon.drawRect( getWidth() - nextMarginRight-healthBarWidth-1,(BAR_HEIGHT-healthBarHeight)/2-1,healthBarWidth+1,  healthBarHeight+1);
		crayon.setColor(Color.RED);
		float healthBarFillWidth = healthBarWidth*(Float.valueOf(player.getCurrentHp())/Float.valueOf(player.getMaximumHP()));
		crayon.fillRect( getWidth() -healthBarWidth- nextMarginRight,(BAR_HEIGHT-healthBarHeight)/2,healthBarFillWidth>0?(int)healthBarFillWidth:0,  healthBarHeight);
	}
	
	/**
	 * methode redefinie de Afficheur retourne une image du jeu
	 */
	@Override
	public void draw(BufferedImage im) {
		Graphics2D crayon = (Graphics2D) im.getGraphics();
		String texture;
		BufferedImage img;

		Map map = game.getMap();
		Point playerPosition = game.getPlayer().getPosition();
		Rectangle camera = new Rectangle(playerPosition.x - width/2, playerPosition.y - height/2, width, height);
		if(camera.x <0){
			camera.x = 0;
		}
		if(camera.y <0) {
			camera.y = 0;
		}
		int diffMaxX = (int)camera.getMaxX() - map.getWidth();
		if(diffMaxX >0) {
			camera.x -= diffMaxX;
		}
		int diffMaxY = (int)camera.getMaxY() - map.getHeigh();
		if(diffMaxY >0) {
			camera.y -= diffMaxY;
		}
		
		for (int y = 0; y < map.getHeigh(); y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				if(camera.contains(x,y)) {
					collisionPainter.drawCollision(crayon, x-camera.x, y-camera.y,0,BAR_HEIGHT, TILE_WIDTH, TILE_HEIGHT, map.getValue(x, y));
				}
				
			}
		}

		for (OnMoveOver m : map.getEvents()) {
			Point position = m.getPosition();
			if(camera.contains(position.x,position.y)) {
				try {
					texture = m.getEffectFactory().get(0).getTexture();
					img = TextureFactory.getInstance().get(texture);
					crayon.drawImage(img,(position.x-camera.x) * TILE_WIDTH, (position.y-camera.y) * TILE_HEIGHT+BAR_HEIGHT, TILE_WIDTH,
							TILE_HEIGHT, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		for (Character c : map.getCharacters()) {
			Point position = c.getPosition();
			if(camera.contains(position.x,position.y)) {
				try {
					texture = c.getTexture();
					
					int imgFrame = c.getDirection().getValue();
					if(c.isType("Monster") && ((Monster)c).isAttacking())
						imgFrame +=4;
					if(c.isType("Player") && ((Pacman)c).isAttacking())
						imgFrame +=4;
					img = TextureFactory.getInstance().get(texture, imgFrame);
					int cPositionX = (position.x-camera.x) * TILE_WIDTH;
					int cPositionY = (position.y-camera.y) * TILE_HEIGHT+BAR_HEIGHT;
					crayon.drawImage(img, cPositionX, cPositionY, 
							TILE_WIDTH, TILE_HEIGHT, null);
					
					int effectX = cPositionX;
					int effectY = cPositionY;
					int effectWidth = TILE_WIDTH/4;
					int effectHeight = TILE_HEIGHT/4;
					for(int i =0; i< c.getEffectsSize();i++) {
						texture = c.getEffect(i).getTexture();
						if(texture != null) {
							img = TextureFactory.getInstance().get(texture);
							crayon.drawImage(img, effectX, effectY, effectWidth, effectHeight, null);
							effectX+=effectWidth;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		drawBar(im);
		
		if(game.getGameState().equalsTo(GameState.State.GAMEOVER)) {
			try {
				img = TextureFactory.getInstance().get("gameover");
				crayon.drawImage(img, (getWidth()-img.getWidth())/2, (getHeight()-img.getHeight())/2, img.getWidth(), img.getHeight(), null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(game.getGameState().equalsTo(GameState.State.WON)) {
			try {
				img = TextureFactory.getInstance().get("won");
				crayon.drawImage(img, (getWidth()-img.getWidth())/2, (getHeight()-img.getHeight())/2, img.getWidth(), img.getHeight(), null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(game.getGameState().equalsTo(GameState.State.STARTING)) {
			Font font = new Font("Verdana", Font.BOLD, 48 - Time.getInstance().getTick()%10*2);
			crayon.setFont(font);
			crayon.setColor(Color.RED);
			crayon.drawString(String.valueOf(3-Time.getInstance().getTick()/10), getWidth()/2, getHeight()/2);
		}
	}

	@Override
	public int getWidth() {
		return width * TILE_WIDTH;
	}

	@Override
	public int getHeight() {
		return height * TILE_HEIGHT+BAR_HEIGHT;
	}

	@Override
	public void setChangeMap() {

	}

}
