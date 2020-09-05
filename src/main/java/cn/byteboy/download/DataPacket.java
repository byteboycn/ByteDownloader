package cn.byteboy.download;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Hong Shaochuan
 * @email xhhsc@outlook.com
 * @date 2020/8/29 17:21
 *
 * 数据包描述
 */
public class DataPacket {

    // 起点索引
    private Long startIndex;

    // 重点索引
    private Long endIndex;

    // 总字节数
    private Long byteSum;

    // 已读取的字节数 (已完成部分)
    private AtomicLong readByteSum;

    // 百分比
    private Double percent;

    private final Lock lock = new ReentrantLock();

    public DataPacket(long startIndex, long endIndex) {
        if (endIndex < startIndex || startIndex < 0) {
            throw new IllegalArgumentException();
        }
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        byteSum = endIndex - startIndex + 1;
        readByteSum = new AtomicLong();
    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public long getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }

    public long getByteSum() {
        return byteSum;
    }

    public void setByteSum(long byteSum) {
        this.byteSum = byteSum;
    }

    public Lock getLock() {
        return lock;
    }

    // 更新已读字节数
    public void update(long len) {
        long read = readByteSum.addAndGet(len);
        this.percent = read / 1d / byteSum * 100d;
    }

    public long getReadByteSum() {
        return readByteSum.get();
    }

    public Double getPercent() {
        return percent == null ? 0d : percent;
    }
}
