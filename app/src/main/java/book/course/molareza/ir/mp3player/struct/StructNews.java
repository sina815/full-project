package book.course.molareza.ir.mp3player.struct;

import android.graphics.Bitmap;

/**
 * Created by Home on 6/7/2016.
 */
public class StructNews {

    public String id;
    public String title;
    public String desc;
    public String text;
    public String thumbnil;
    public String bigImage;
    public Bitmap thBitmap;
    public int like;
    public int visit;
    public int share;

    public String getId() {
        return id;
    }

    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getThumbnil() {
        return thumbnil;
    }

    public void setThumbnil(String thumbnil) {
        this.thumbnil = thumbnil;
    }

    public Bitmap getThBitmap() {
        return thBitmap;
    }

    public void setThBitmap(Bitmap thBitmap) {
        this.thBitmap = thBitmap;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getVisit() {
        return visit;
    }

    public void setVisit(int visit) {
        this.visit = visit;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }
}
