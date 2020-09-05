package cn.byteboy.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Hong Shaochuan
 * @email xhhsc@outlook.com
 * @date 2020/8/29 16:57
 *
 * 下载器
 * 提供具体的下载服务
 */
public class BIODownloader implements Runnable, Downloader {


    private DownloadTask task;

    // 线程当前处理的dataPacket
    private DataPacket dataPacket;

    private CountDownLatch latch;

    // 计算下载速度
    // 采样数量，每份时间内下载的字节数
    private double[] speedArr = new double[6];

    // 速度采集间隔，纳秒 (500ms)
    private static final long SPEED_COLLECT_INTERVAL = 500 * 1000000;

    // 瞬时速度, byte / s
    private double speed;

    private int speedIndex = 0;

    public BIODownloader(DownloadTask task) {
        this.task = task;
    }

    public void assignTask(DownloadTask task) {
        this.task = task;
    }


    public void download() {
        if (task == null) {
            throw new IllegalArgumentException("当前没有要下载的任务");
        }
        // 检查任务是否还需要下载
        if (!didTaskDownload()) {
            throw new IllegalArgumentException("当前没有要下载的任务");
        }
        assignDataPacket();
        if (dataPacket == null) {
            throw new IllegalArgumentException("当前没有要下载的任务");
        }
//        System.out.println(dataPacket);
        try {
            URL url = new URL(task.getServerPath());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range", "bytes=" + dataPacket.getStartIndex() + "-" + dataPacket.getEndIndex());
            connection.setConnectTimeout(5000);
            int code = connection.getResponseCode();
            byte[] buffer = new byte[1024];
            int len;
            InputStream inputStream = connection.getInputStream();
            RandomAccessFile raf = new RandomAccessFile(task.getLocalPath(), "rwd");
            raf.seek(dataPacket.getStartIndex());
            // 计算下载速度
            long startTime = System.nanoTime();
            long timeDiff;
            long tempLen = 0;
            while ((len = inputStream.read(buffer)) != -1) {

                dataPacket.update(len);
                raf.write(buffer, 0, len);
                tempLen += len;
                timeDiff = System.nanoTime() - startTime;
                if (timeDiff >= SPEED_COLLECT_INTERVAL) {
                    startTime = System.nanoTime();
                    double second = timeDiff / 1000000000d;
                    speed = tempLen / 1d / second;
                    tempLen = 0;
                    speedArr[speedIndex++] = speed;
                    if (speedIndex >= speedArr.length) {
                        speedIndex = 0;
                    }
                }
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dataPacket.getLock().unlock();
        }
    }

    // 分配dataPacket
    private void assignDataPacket() {
        if (task == null) {
            return;
        }
        for (DataPacket packet : task.getDataPackets()) {
            if (packet.getLock().tryLock()) {
                dataPacket = packet;
                break;
            }
        }
    }

    // 检查任务是否需要下载
    private boolean didTaskDownload () {
        if (task.getStatue() == DownloadTask.RUNNING) {
            long byteReadSum = 0;
            for (DataPacket packet : task.getDataPackets()) {
                byteReadSum += packet.getReadByteSum();
            }
            if (byteReadSum == task.getByteSum()) {
                task.setStatue(DownloadTask.FINISH);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void run() {
        download();
    }


    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public double getAverageSpeed() {
        double s = 0d;
        for (double v : speedArr) {
            s += v;
        }
        return s / speedArr.length;
    }

    @Override
    public double getPercent() {
        return dataPacket.getPercent();
    }

    @Override
    public long getByteSum() {
        return dataPacket.getByteSum();
    }

    public long getReadByteSum() {
        return dataPacket.getReadByteSum();
    }


}
