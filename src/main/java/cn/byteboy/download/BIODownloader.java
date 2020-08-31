package cn.byteboy.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Hong Shaochuan
 * @email xhhsc@outlook.com
 * @date 2020/8/29 16:57
 *
 * 下载器
 * 提供具体的下载服务
 */
public class BIODownloader extends Observable implements DownloadService {

    // 下载地址
    private String serverPath;

    // 本地保存地址
    private String localPath;

    private DataPacket dataPacket;

    private CountDownLatch latch;

    // 计算下载速度
    // 采样时间 50ms
    private long interval = 100;

    // 采样数量，每份时间内下载的字节数
    private double[] speedArr = new double[6];

    // 速度采集间隔，纳秒 (500ms)
    private long speedCollectInterval = 500 * 1000000;

    // 瞬时速度, byte / s
    private double speed;

    private int speedIndex = 0;

    public BIODownloader(String serverPath, String localPath, DataPacket dataPacket, CountDownLatch latch) {
        this.serverPath = serverPath;
        this.localPath = localPath;
        this.dataPacket = dataPacket;
        this.latch = latch;
    }

    public void download() {
        try {
            URL url = new URL(serverPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range", "bytes=" + dataPacket.getStartIndex() + "-" + dataPacket.getEndIndex());
            connection.setConnectTimeout(5000);
            int code = connection.getResponseCode();
            byte[] buffer = new byte[1024];
            int len;
            InputStream inputStream = connection.getInputStream();
            RandomAccessFile raf = new RandomAccessFile(localPath, "rwd");
            raf.seek(dataPacket.getStartIndex());
            // 计算下载速度
            long startTime = System.nanoTime();
            long timeDiff;
            long tempLen = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                setChanged();
                notifyObservers(len);
//                dataPacket.update(len);
                raf.write(buffer, 0, len);
                tempLen += len;
                timeDiff = System.nanoTime() - startTime;
                if (timeDiff >= speedCollectInterval) {
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
            latch.countDown();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
