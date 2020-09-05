package cn.byteboy.download;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Hong Shaochuan
 * @email xhhsc@outlook.com
 * @date 2020/8/30 23:46
 *
 * 下载任务
 */
public class DownloadTask implements Serializable {

    private static final long serialVersionUID = 8241970228716425282L;

    // 任务id
    private String id;

    // 任务状态
    private volatile int statue;

    // 已创建
    transient static final int CREATED = 1;

    // 下载中
    transient static final int RUNNING = 2;

    // 已暂停
    transient static final int PAUSE = 3;

    // 已完成
    transient static final int FINISH = 4;

    // 下载出错
    transient static final int ERROR = 5;

    // 已删除
    transient static final int DELETED = 6;

    // 下载地址
    private String serverPath;

    // 保存地址
    private String localPath;

    // 下载线程数
    private int nThread;

    // 是否支持断点续传
    private boolean resume;

    // 总字节数
    private long byteSum;

    // 数据包描述
    private List<DataPacket> dataPackets;

    public DownloadTask(String serverPath) {
        this(1, serverPath);
    }

    public DownloadTask(int nThread, String serverPath) {
        this(nThread, serverPath, null);
    }

    public DownloadTask(int nThread, String serverPath, String localPath) {
        this.nThread = nThread;
        this.serverPath = serverPath;
        this.localPath = localPath;
        this.dataPackets = new CopyOnWriteArrayList<>();
        this.statue = CREATED;
    }



    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public int getNThread() {
        return nThread;
    }

    public void setNThread(int nThread) {
        this.nThread = nThread;
    }

    public boolean isResume() {
        return resume;
    }

    public void setResume(boolean resume) {
        this.resume = resume;
    }

    public long getByteSum() {
        return byteSum;
    }

    public void setByteSum(long byteSum) {
        this.byteSum = byteSum;
    }

    public List<DataPacket> getDataPackets() {
        return dataPackets;
    }

    public void setDataPackets(List<DataPacket> dataPackets) {
        this.dataPackets = dataPackets;
    }


    public void addDataPacket(DataPacket packet) {
        if (dataPackets.size() >= nThread) {
            throw new RuntimeException("我都满了，不能再添加了");
        }
        dataPackets.add(packet);
    }

}
