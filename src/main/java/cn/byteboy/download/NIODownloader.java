package cn.byteboy.download;

/**
 * @author Hong Shaochuan
 * @email xhhsc@outlook.com
 * @date 2020/8/31 23:25
 */
public class NIODownloader implements Downloader {

    @Override
    public double getSpeed() {
        return 0;
    }

    @Override
    public double getAverageSpeed() {
        return 0;
    }

    @Override
    public double getPercent() {
        return 0;
    }

    @Override
    public long getByteSum() {
        return 0;
    }
}
