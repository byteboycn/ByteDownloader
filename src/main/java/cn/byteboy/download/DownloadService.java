package cn.byteboy.download;

/**
 * @author Hong Shaochuan
 * @email xhhsc@outlook.com
 * @date 2020/8/31 00:07
 */
public interface DownloadService {

    /**
     * 获取瞬时下载速度 (B/s)
     *
     * @return speed
     */
    double getSpeed();

    /**
     * 获取平均下载速度 (B/s)
     *
     * @return average speed
     */
    double getAverageSpeed();

    /**
     * 获取下载进度百分比
     *
     * @return percent
     */
    double getPercent();

    /**
     * 获取总字节数
     *
     * @return byte sum
     */
    long getByteSum();
}
