package cn.byteboy.view;

import io.termd.core.tty.TtyConnection;

/**
 * @author Hong Shaochuan
 * @email xhhsc@outlook.com
 * @date 2020/9/5 01:26
 */
public class DashboardView {

    private final TtyConnection conn;

    public DashboardView(TtyConnection conn) {
        this.conn = conn;
    }

    public void draw() {

    }

    public void handle() {
        System.out.println("handle");
    }
}
