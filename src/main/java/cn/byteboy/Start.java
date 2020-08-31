package cn.byteboy;

import cn.byteboy.download.Download;
import cn.byteboy.download.ProgressBar;

/**
 * @author Hong Shaochuan
 * @email xhhsc@outlook.com
 * @date 2020/8/29 16:55
 */
public class Start {

    public static void main(String[] args) {
//        http://02.cdl1-pcgame.720582.com:8090/yxdown.com_LittleNightmaresSecretsofTheMawChapter2_cht.rar
//        new Download("http://c.hsc/small.cdr").download();
//        new Download(5, "http://c.hsc/small.cdr", "aa.cdr").download();
        new Download(5, "http://c.hsc/small.cdr", "logs/aa.cdr").download();
//        new Download(8, "http://c.hsc/test.mp4").download();

    }
}
