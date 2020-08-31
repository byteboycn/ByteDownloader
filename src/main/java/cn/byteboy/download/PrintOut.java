package cn.byteboy.download;

import java.util.List;

/**
 * @author Hong Shaochuan
 * @email xhhsc@outlook.com
 * @date 2020/8/29 21:56
 *
 * 打印输出
 */
public class PrintOut extends Thread implements DownloadService {
    // 总字节数
    private long byteSum;

    // 已读取字节数
    private volatile long byteReadSum;



    // 输出行
    private StringBuilder lineShow = new StringBuilder();


    // 计算下载速度
    // 采样时间 50ms
    private final long INTERVAL = 100;


    private List<BIODownloader> downloaderList;

    private ProgressBar bar = new ProgressBar();


    public PrintOut(List<BIODownloader> downloaderList) {
        super("PrintOut");
        this.downloaderList = downloaderList;
        for (BIODownloader downloader : downloaderList) {
            byteSum += downloader.getByteSum();
        }
    }



    @Override
    public void run() {
        while (!interrupted()) {
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            print();
        }
    }


    /**
     * 打印
     */
    private void print() {

        printDelete(lineShow.length());
        lineShow.delete(0, lineShow.length());

        double percent = getPercent();
        lineShow.append(String.format("%.2f", percent)).append("%")
                .append(bar.getBar((int) percent))
//                .append(String.format("%.2f",  getSpeed()));
                .append(String.format("(%s/s)", getSizeWithUnit((long) getSpeed())))
                .append(String.format("(%s/%s)", getSizeWithUnit(byteReadSum), getSizeWithUnit(byteSum)));
        System.out.print(lineShow.toString());
        if (percent == 100) {
            interrupt();
        }


    }

    // 打印退格符
    private void printDelete(int n) {
        for (int i = 0; i < n; i++) {
            System.out.print('\b');
        }
    }


    @Override
    public double getPercent() {
        long readSum = 0L;
        for (BIODownloader downloader : downloaderList) {
            readSum += downloader.getReadByteSum();
        }
        // 刷新总读取量，放在这，不合理
        byteReadSum = readSum;
        return readSum / 1d / byteSum * 100d;
    }

    @Override
    public double getSpeed() {
        double s = 0d;
        for (BIODownloader downloader : downloaderList) {
            s += downloader.getAverageSpeed();
        }
        return s;
    }

    @Override
    public double getAverageSpeed() {
        return getSpeed();
    }

    @Override
    public long getByteSum() {
        return byteSum;
    }

    public static String getSizeWithUnit(long size) {
        if ( size >= 1024*1024*1024 ){
            return String.format("%.2fGB", size/1024d/1024d/1024d);

        } else if ( size >= 1024*1024 ){
            return String.format("%.2fMB", size/1024d/1024d);

        } else if ( size >= 1024 ){
            return size/1024 + "KB";
        }else {
            return size+"B";
        }
    }



}
