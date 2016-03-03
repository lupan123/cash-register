# cash-register
收银机打印清单小模块。

运行方式：
1.运行modules文件夹下面的main类。

总体设计：
1.PrintMachine代表打印机，运行时新建实例，先初始化获取商店商品清单，再获取传过来的二维码，最后打印结果。
2.商店商品清单、结算商品条码都用json文件存储。
3.Item类中添加promotionTypes来判断商品正在进行的优惠活动。
