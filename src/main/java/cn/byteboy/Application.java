package cn.byteboy;

import cn.byteboy.download.DownloadManager;
import cn.byteboy.download.DownloadTask;
import cn.byteboy.download.ProgressBar;
import cn.byteboy.view.DashboardView;
import examples.events.EventsExample;
import io.termd.core.ssh.netty.NettySshTtyBootstrap;
import io.termd.core.tty.TtyConnection;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Hong Shaochuan
 * @email xhhsc@outlook.com
 * @date 2020/8/29 16:55
 */
public class Start {

    public synchronized static void main(String[] args) throws Exception {
//        http://02.cdl1-pcgame.720582.com:8090/yxdown.com_LittleNightmaresSecretsofTheMawChapter2_cht.rar
//        new Download("http://c.hsc/small.cdr").download();
//        new Download(5, "http://c.hsc/small.cdr", "aa.cdr").download();
//        new Download(5, "http://c.hsc/small.cdr", "logs/aa.cdr").download();
//        new Download(8, "http://c.hsc/test.mp4").download();


//        NettySshTtyBootstrap bootstrap = new NettySshTtyBootstrap().setHost("localhost").setPort(4000);
//        try {
//            bootstrap.start(conn -> new DashboardView(conn).handle()).get(10, TimeUnit.SECONDS);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("Telnet server started on localhost:4000");

        NettySshTtyBootstrap bootstrap = new NettySshTtyBootstrap().setHost("localhost").setPort(4000);
        bootstrap.start(EventsExample::handle).get(10, TimeUnit.SECONDS);
        System.out.println("Telnet server started on localhost:4000");

        try {
            Start.class.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        DownloadManager dm = new DownloadManager();
//        dm.addTask(new DownloadTask(10, "http://c.hsc/test.mp4", "logs/test.mp4"));
//        dm.start();

    }
}
