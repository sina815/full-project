package book.course.molareza.ir.mp3player.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "LIKE_MUSIC_KHAREJI".
 */
public class LikeMusicKhareji {

    private Long id;
    private String item_id;

    public LikeMusicKhareji() {
    }

    public LikeMusicKhareji(Long id) {
        this.id = id;
    }

    public LikeMusicKhareji(Long id, String item_id) {
        this.id = id;
        this.item_id = item_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

}
