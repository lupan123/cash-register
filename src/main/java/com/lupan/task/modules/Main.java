package com.lupan.task.modules;

/**
 * TODO 程序运行主类
 *
 * @author lupan
 * @version 2016/2/29 0029
 */
public class Main {

    public static void main(String[] args) throws Exception {
        PrintMachine pm = new PrintMachine();
        pm.init();
        pm.getBarCodes("src/main/resources/barCodesList.json");
        pm.print();
        //asdfad
    }
}
