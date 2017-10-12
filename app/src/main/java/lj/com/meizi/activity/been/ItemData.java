package lj.com.meizi.activity.been;

import java.io.Serializable;

/**
 * Created by LJ on 2016/9/9.
 */
public class ItemData implements Serializable {
    private String title;
    private String url;
    private String intro;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
