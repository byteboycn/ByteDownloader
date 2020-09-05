//package cn.byteboy.download;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//
///**
// * @author Hong Shaochuan
// * @email xhhsc@outlook.com
// * @date 2020/8/29 17:13
// *
// * 下载
// * 将下载任务分配给下载器下载
// */
//public class Download {
//
//    // 下载线程数
//    private int nThread;
//
//    // 下载地址
//    private String serverPath;
//
//    // 本地保存地址
//    private String localPath;
//
//    // 总数据量
//    private long byteSum;
//
//    // 已下载字节量
//    private long byteReadSum;
//
//    private List<DataPacket> dataPackets;
//
//    public Download(String serverPath) {
//        this(1, serverPath);
//    }
//
//    public Download(int nThread, String serverPath) {
//        this(nThread, serverPath, null);
//    }
//
//    public Download(int nThread, String serverPath, String localPath) {
//        this.nThread = nThread;
//        this.serverPath = serverPath;
//        this.localPath = localPath;
//        this.dataPackets = new ArrayList<>(nThread);
//        if (localPath == null || localPath.equals("")) {
//            if (serverPath.lastIndexOf('/') != -1) {
//                this.localPath = serverPath.substring(serverPath.lastIndexOf('/') + 1);
//            } else {
//                throw new IllegalArgumentException();
//            }
//        }
//
//        init();
//    }
//
//    private void init() {
//        try {
//            URL url = new URL(serverPath);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setConnectTimeout(5000);
//            int code = connection.getResponseCode();
//            if (code == 200) {
//                long length = connection.getContentLengthLong();
//                byteSum = length;
//                long blockSize = length / nThread;
//                for (int i = 0; i < nThread; i++) {
//                    long startIndex = i * blockSize;
//                    long endIndex =  startIndex + blockSize - 1;
//                    if (i == nThread - 1) {
//                        //最后一个线程下载的长度稍微长一点
//                        endIndex = length - 1;
//                    }
//                    dataPackets.add(new DataPacket(startIndex, endIndex));
//                }
//
//            } else {
//                System.out.println("初始化失败，错误代码：" + code);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
////    public void download() {
////        CountDownLatch latch = new CountDownLatch(nThread);
////        List<BIODownloader> downloaderList = new ArrayList<>(nThread);
////        for (DataPacket dataPacket : dataPackets) {
////            BIODownloader downloader = new BIODownloader(serverPath, localPath, dataPacket, latch);
////            downloaderList.add(downloader);
////        }
////        new PrintOut(downloaderList).start();
////        for (final BIODownloader downloader : downloaderList) {
////            new Thread(downloader::download).start();
////        }
////        try {
////            latch.await();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////    }
//}
