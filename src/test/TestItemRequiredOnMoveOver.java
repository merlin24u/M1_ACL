package test;

import model.*;
import model.movable.character.Character;
import model.movable.character.Pacman;
import model.onmoveover.ItemRequiredOnMoveOver;
import model.item.Item;
import model.item.Key;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestItemRequiredOnMoveOver {

	Point point;
	boolean isVisible;
	boolean isActivated;
	boolean isPersistingAfterActivation;
	ArrayList<Item> itemsRequired;
	boolean removeRequirements;

	Map map;
	ItemRequiredOnMoveOver itemRequiredOnMoveOver;
	Key key;
	Pacman pacman;

	@Before
	public void initialize() {

		PacmanGame pacmanGame = new PacmanGame("test_map.xml");
		map = pacmanGame.getMap();

		point = new Point(4, 4);
		isVisible = true;
		isActivated = true;
		isPersistingAfterActivation = true;
		itemsRequired = new ArrayList<Item>();
		removeRequirements = true;

	}

	@Test
	public void testHasRequirements() {

		for (Character character : map.getCharacters()) {
			if (character instanceof Pacman) {
				pacman = (Pacman) character;
			}
		}

		key = new Key("Id_1", "Name_1");
		itemsRequired.add(key);

		pacman.addItem(key);
		assertEquals("Pacman n'a pas de cle!", key, pacman.getItem(key.getId()));

		itemRequiredOnMoveOver = new ItemRequiredOnMoveOver(map, point, isVisible, isActivated,
				isPersistingAfterActivation, itemsRequired, removeRequirements);

		itemRequiredOnMoveOver.update();
		assertNull("Le cle n'a pas ete supprimer de pacman sac a dos!", pacman.getItem(key.getId()));
	}

	@Test
	public void testHasTwoRequirements() {

		for (Character character : map.getCharacters()) {
			if (character instanceof Pacman) {
				pacman = (Pacman) character;
			}
		}

		key = new Key("Id_1", "Name_1");
		itemsRequired.add(key);

		pacman.addItem(key);
		assertEquals("Pacman n'a pas de cle!", key, pacman.getItem(key.getId()));

		key = new Key("Id_2", "Name_2");
		itemsRequired.add(key);

		pacman.addItem(key);
		assertEquals("Pacman n'a pas de cle!", key, pacman.getItem(key.getId()));

		itemRequiredOnMoveOver = new ItemRequiredOnMoveOver(map, point, isVisible, isActivated,
				isPersistingAfterActivation, itemsRequired, removeRequirements);

		itemRequiredOnMoveOver.update();
		assertNull("Le cle n'a pas ete supprimer de pacman sac a dos!", pacman.getItem(key.getId()));
	}

	@Test
	public void testNoHasRequirements() {

		for (Character character : map.getCharacters()) {
			if (character instanceof Pacman) {
				pacman = (Pacman) character;
			}
		}

		key = new Key("Id_1", "Name_1");
		itemsRequired.add(key);

		pacman.addItem(key);
		assertEquals("Pacman n'a pas de cle!", key, pacman.getItem(key.getId()));

		key = new Key("Id_2", "Name_2");
		itemsRequired.add(key);

		itemRequiredOnMoveOver = new ItemRequiredOnMoveOver(map, point, isVisible, isActivated,
				isPersistingAfterActivation, itemsRequired, removeRequirements);

		itemRequiredOnMoveOver.update();
		assertEquals("Pacman cle 1 a ete supprime!", itemsRequired.get(0),
				pacman.getItem(itemsRequired.get(0).getId()));
		assertNull("Pacman a cle 2!", pacman.getItem(itemsRequired.get(1).getId()));
	}
}
