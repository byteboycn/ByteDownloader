package cn.byteboy.download;

/**
 * @author Hong Shaochuan
 * @email xhhsc@outlook.com
 * @date 2020/8/29 18:36
 */
public class ProgressBar {

    private int index = 0;

    private String target;


    // 进度条粒度 (PROGRESS_SIZE * BITE = 100)
    private final int PROGRESS_SIZE = 50;
    private int BITE = 2;

    private String getNChar(int num, char ch){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < num; i++){
            builder.append(ch);
        }
        return builder.toString();
    }

    public void printProgress() throws InterruptedException {
        System.out.print("Progress:");

        target = String.format("%3d%%├%s%s┤", index, getFinish(), getUnFinish());
        System.out.print(target);

        while (index <= 100){
            target = String.format("%3d%%├%s%s┤", index, getFinish(), getUnFinish());
            System.out.print(getNChar(PROGRESS_SIZE + 6, '\b'));
            System.out.print(target);
            Thread.sleep(200);
            index++;
        }
    }

    private String getFinish() {
        return getNChar(index / BITE, '█');
    }

    private String getUnFinish() {
        return getNChar(PROGRESS_SIZE - index / BITE, '─');
    }

    public String getBar(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("0 < percent < 100");
        }
        index = percent;
        return String.format("├%s%s┤", getFinish(), getUnFinish());
    }


}
