package ir.molareza.mp3player.db;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Main {

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(7, "book.course.molareza.ir.mp3player.db");

        Entity favoriteDetail = schema.addEntity("FavoriteDetail");
        favoriteDetail.addIdProperty().primaryKey();
        favoriteDetail.addStringProperty("name");
        favoriteDetail.addStringProperty("id_item");
        favoriteDetail.addStringProperty("album");
        favoriteDetail.addStringProperty("thImage");
        favoriteDetail.addStringProperty("bigImage");
        favoriteDetail.addStringProperty("text");

        Entity favoriteMusicIrani = schema.addEntity("FavoriteMusicIrani");
        favoriteMusicIrani.addIdProperty().primaryKey();
        favoriteMusicIrani.addStringProperty("name");
        favoriteMusicIrani.addStringProperty("id_item");
        favoriteMusicIrani.addStringProperty("album");
        favoriteMusicIrani.addStringProperty("thImage");
        favoriteMusicIrani.addStringProperty("bigImage");
        favoriteMusicIrani.addStringProperty("mp3_64");
        favoriteMusicIrani.addStringProperty("mp3_128");
        favoriteMusicIrani.addStringProperty("table");

        Entity favoriteMusicKhareji = schema.addEntity("FavoriteMusicKhareji");
        favoriteMusicKhareji.addIdProperty().primaryKey();
        favoriteMusicKhareji.addStringProperty("name");
        favoriteMusicKhareji.addStringProperty("id_item");
        favoriteMusicKhareji.addStringProperty("album");
        favoriteMusicKhareji.addStringProperty("thImage");
        favoriteMusicKhareji.addStringProperty("bigImage");
        favoriteMusicKhareji.addStringProperty("mp3_64");
        favoriteMusicKhareji.addStringProperty("mp3_128");
        favoriteMusicKhareji.addStringProperty("table");

        Entity favoriteClip = schema.addEntity("FavoriteClip");
        favoriteClip.addIdProperty().primaryKey();
        favoriteClip.addStringProperty("id_item");
        favoriteClip.addStringProperty("name");
        favoriteClip.addStringProperty("album");
        favoriteClip.addStringProperty("thImage");
        favoriteClip.addStringProperty("bigImage");
        favoriteClip.addStringProperty("clip");


        Entity likeDetail = schema.addEntity("LikeDetail");
        likeDetail.addIdProperty().primaryKey();
        likeDetail.addStringProperty("item_id");

        Entity likeMusicIrani = schema.addEntity("LikeMusicIrani");
        likeMusicIrani.addIdProperty().primaryKey();
        likeMusicIrani.addStringProperty("item_id");

        Entity likeMusicKhareji = schema.addEntity("LikeMusicKhareji");
        likeMusicKhareji.addIdProperty().primaryKey();
        likeMusicKhareji.addStringProperty("item_id");

        Entity likeClip = schema.addEntity("LikeClip");
        likeClip.addIdProperty().primaryKey();
        likeClip.addStringProperty("item_id");

        Entity likeOtherApp = schema.addEntity("LikeOtherApp");
        likeOtherApp.addIdProperty().primaryKey();
        likeOtherApp.addStringProperty("item_id");

        DaoGenerator dg = new DaoGenerator();
        dg.generateAll(schema, "app/src/main/java");

    }
}
