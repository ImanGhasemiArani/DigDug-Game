package ir.ac.kntu.gameObjects.enemy;

import ir.ac.kntu.controller.ai.AISimpleEnemy;
import ir.ac.kntu.gameData.GameData;
import ir.ac.kntu.gameObjects.MovingGameObject;
import ir.ac.kntu.picture.PictureBuilder;



public class SimpleEnemy extends Enemy implements MovingGameObject, EnemyInterface {

    public SimpleEnemy(int x, int y) {
        super(x, y, GameData.ENEMY_SIMPLE,
                PictureBuilder.RIGHT_STAND_IMAGE_SIMPLE, PictureBuilder.RIGHT_RUN_IMAGE_SIMPLE, PictureBuilder.LEFT_STAND_IMAGE_SIMPLE, PictureBuilder.LEFT_RUN_IMAGE_SIMPLE,
                PictureBuilder.UP_STAND_IMAGE_SIMPLE, PictureBuilder.UP_RUN_IMAGE_SIMPLE, PictureBuilder.DOWN_STAND_IMAGE_SIMPLE, PictureBuilder.DOWN_RUN_IMAGE_SIMPLE,
                PictureBuilder.INFLATING_1_RIGHT_IMAGE_SIMPLE, PictureBuilder.INFLATING_1_LEFT_IMAGE_SIMPLE, PictureBuilder.INFLATING_1_UP_IMAGE_SIMPLE, PictureBuilder.INFLATING_1_DOWN_IMAGE_SIMPLE,
                PictureBuilder.INFLATING_2_RIGHT_IMAGE_SIMPLE, PictureBuilder.INFLATING_2_LEFT_IMAGE_SIMPLE, PictureBuilder.INFLATING_2_UP_IMAGE_SIMPLE, PictureBuilder.INFLATING_2_DOWN_IMAGE_SIMPLE,
                PictureBuilder.INFLATING_3_RIGHT_IMAGE_SIMPLE, PictureBuilder.INFLATING_3_LEFT_IMAGE_SIMPLE, PictureBuilder.INFLATING_3_UP_IMAGE_SIMPLE, PictureBuilder.INFLATING_3_DOWN_IMAGE_SIMPLE);
        configureAI(new AISimpleEnemy(this));
    }
}








