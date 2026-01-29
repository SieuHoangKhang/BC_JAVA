package noithat;

import noithat.views.FormDangNhap;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FormDangNhap form = new FormDangNhap();
                form.setVisible(true);
            }
        });
    }
}
