package book.course.molareza.ir.mp3player.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "FAVORITE_MUSIC_IRANI".
 */
public class FavoriteMusicIrani {

    private Long id;
    private String name;
    private String id_item;
    private String album;
    private String thImage;
    private String bigImage;
    private String mp3_64;
    private String mp3_128;
    private String table;

    public FavoriteMusicIrani() {
    }

    public FavoriteMusicIrani(Long id) {
        this.id = id;
    }

    public FavoriteMusicIrani(Long id, String name, String id_item, String album, String thImage, String bigImage, String mp3_64, String mp3_128, String table) {
        this.id = id;
        this.name = name;
        this.id_item = id_item;
        this.album = album;
        this.thImage = thImage;
        this.bigImage = bigImage;
        this.mp3_64 = mp3_64;
        this.mp3_128 = mp3_128;
        this.table = table;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getThImage() {
        return thImage;
    }

    public void setThImage(String thImage) {
        this.thImage = thImage;
    }

    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    public String getMp3_64() {
        return mp3_64;
    }

    public void setMp3_64(String mp3_64) {
        this.mp3_64 = mp3_64;
    }

    public String getMp3_128() {
        return mp3_128;
    }

    public void setMp3_128(String mp3_128) {
        this.mp3_128 = mp3_128;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

}
