
package book.course.molareza.ir.mp3player.struct;


import android.graphics.Bitmap;

public class StructMusic {

    public String id;
    public String name;
    public String album;
    public String thumbnile;
    public Bitmap thBitmap;
    public String bigimage;
    public Bitmap bigBitmap;
    public String mp364;
    public String mp3128;
    public String cat;
    public String idcat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getThumbnile() {
        return thumbnile;
    }

    public void setThumbnile(String thumbnile) {
        this.thumbnile = thumbnile;
    }

    public String getBigimage() {
        return bigimage;
    }

    public void setBigimage(String bigimage) {
        this.bigimage = bigimage;
    }

    public String getMp364() {
        return mp364;
    }

    public void setMp364(String mp364) {
        this.mp364 = mp364;
    }

    public String getMp3128() {
        return mp3128;
    }

    public void setMp3128(String mp3128) {
        this.mp3128 = mp3128;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getIdcat() {
        return idcat;
    }

    public void setIdcat(String idcat) {
        this.idcat = idcat;
    }
}

