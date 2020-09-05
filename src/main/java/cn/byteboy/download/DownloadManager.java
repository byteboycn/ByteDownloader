package cn.byteboy.download;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Hong Shaochuan
 * @email xhhsc@outlook.com
 * @date 2020/8/31 23:23
 */
public class DownloadManager {

    // 下载任务
    private List<DownloadTask> downloadTasks;

    private final ExecutorService threadPool = Executors.newFixedThreadPool(30);

    public DownloadManager() {
        downloadTasks = new CopyOnWriteArrayList<>();
    }

    public DownloadManager addTask(DownloadTask task) {
        downloadTasks.add(task);
        return this;
    }

    public void start() {
        for (DownloadTask task : downloadTasks) {
            parseTask(task);
        }
        download();

    }

    // 解析任务
    private void parseTask(DownloadTask task) {
        try {
            URL url = new URL(task.getServerPath());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            int code = connection.getResponseCode();
            if (code == 200) {
                long length = connection.getContentLengthLong();
                int nThread = task.getNThread();
                task.setByteSum(length);
                long blockSize = length / nThread;
                for (int i = 0; i < nThread; i++) {
                    long startIndex = i * blockSize;
                    long endIndex =  startIndex + blockSize - 1;
                    if (i == nThread - 1) {
                        //最后一个线程下载的长度稍微长一点
                        endIndex = length - 1;
                    }
                    task.addDataPacket(new DataPacket(startIndex, endIndex));
                }
            } else {
                throw new RuntimeException("解析下载地址出错");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("下载任务失败，地址：" + task.getServerPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void download() {
        for (DownloadTask task : downloadTasks) {
            if (task.getStatue() == DownloadTask.CREATED) {
                task.setStatue(DownloadTask.RUNNING);
                for (int i = 0; i < task.getNThread(); i++) {
                    threadPool.submit(new BIODownloader(task));
                }
            }
        }
    }




}
