package com.lupan.task.modules;

import com.alibaba.fastjson.JSON;
import com.lupan.task.entity.Item;
import com.lupan.task.entity.PromotionTypeConstant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.*;

/**
 * TODO 清单打印机
 *
 * @author lupan
 * @version 2016/2/29 0029
 */
public class PrintMachine {

    //折扣率
    public static final double DISCOUNT_RATE = 0.95;
    //商场商品清单数据文件
    public static final String ITEM_LIST_FILE = "src/main/resources/itemList.json";
    //结账商品
    private List<Item> payList;
    //商店商品清单
    private Map<String,Item> itemListMap;
    //条形码清单
    private List<String> barCodeList;


    /**
     * 初始化化打印机
     */
    public void init() throws Exception {
        payList = new ArrayList<>();
        List<Item> itemList = readData(ITEM_LIST_FILE, Item.class);
        itemListMap = listToMap(itemList);
    }



    /**
     * 读取条形码清单，并解析
     * @param barCodesPath 条形码清单路径
     */
    public void getBarCodes(String barCodesPath) throws Exception {
        barCodeList = readData(barCodesPath, String.class);
        //将条形码清单转为商品清单
        getItemInfoByBarCode();
    }

    /**
     * 打印清单
     */
    public void print() {
        //实际付款总价
        double sum = 0;
        //节省总价
        double saving = 0;
        //赠送的物品
        List<Item> tempList = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("***<没钱赚商店>购物清单***\n");

        //每类商品应当付款的量
        double payAmount;
        //每类商品免费的量
        double savedAmount;

        for (Item item : payList) {

            //是否买一赠一
            if (item.getPromotionType().contains(PromotionTypeConstant.BUY_TWO_GIVE_ONE)) {

                payAmount = (item.getAmount() - (int) item.getAmount() / 3);
                sb.append(String.format("名称：%s，数量：%.0f%s，单价：%.2f(元)，小计：%.2f(元)\n",
                        item.getName(), item.getAmount(), item.getUnit(), item.getPrice(), item.getPrice() * payAmount));

                //添加总价
                sum += payAmount * item.getPrice();
                savedAmount = (int) item.getAmount() / 3;
                saving += savedAmount * item.getPrice();
                if (savedAmount > 0) {
                    item.setAmount(savedAmount);
                    tempList.add(item);
                }
                continue;
            }


            //是否打折
            if ((item.getPromotionType().contains(PromotionTypeConstant.DISCOUNT))) {
                sb.append(String.format("名称：%s，数量：%.0f%s，单价：%.2f(元)，小计：%.2f(元)，节省%.2f(元)\n",
                        item.getName(), item.getAmount(), item.getUnit(), item.getPrice(), item.getPrice() * item.getAmount() * DISCOUNT_RATE, item.getPrice() * item.getAmount() * (1 - DISCOUNT_RATE)));
                //添加总价
                sum += item.getAmount() * item.getPrice() * DISCOUNT_RATE;
                saving += item.getAmount() * item.getPrice() * (1 - DISCOUNT_RATE);
                continue;
            }

            //没有优惠
            sb.append(String.format("名称：%s，数量：%.0f%s，单价：%.2f(元)，小计：%.2f(元)\n",
                    item.getName(), item.getAmount(), item.getUnit(), item.getPrice(), item.getPrice() * item.getAmount()));
            //添加总价
            sum += item.getAmount() * item.getPrice();
        }

        //赠送部分打印
        if (tempList.size() > 0) {
            sb.append("----------------------\n买二赠一商品：\n");
            for (Item item : tempList) {
                sb.append(String.format("名称：%s，数量：%.0f%s\n", item.getName(), item.getAmount(), item.getUnit()));
            }
        }

        sb.append(String.format("----------------------\n总计：%.2f(元)\n", sum));
        //节省部分
        if (saving > 0) {
            sb.append(String.format("节省：%.2f(元)\n", saving));
        }
        sb.append("**********************\n");

        //打印结果
        System.out.print(sb.toString());
    }


    /**
     * 将list转为map
     * @param itemList
     * @return
     */
    private Map<String,Item> listToMap(List<Item> itemList){
        Map<String,Item> map = new HashMap<>();
        for(Item item : itemList){
            map.put(item.getBarCode(),item);
        }
        return map;
    }


    /**
     * 通过条形码获取商品条目信息
     */
    private void getItemInfoByBarCode() {
        Map<String, Item> addedItemMap = new LinkedHashMap<>();
        for (String barCode : barCodeList) {
                //找到清单中相应的条目
                Item item = itemListMap.get(barCode.substring(0, 10));
                if (item!=null) {

                    if (barCode.length() > 10) {
                        item.setAmount(Double.parseDouble(barCode.split("-")[1]));
                    } else {
                        item.setAmount(1);
                    }

                    //判断是否添加
                    Item addedItem = addedItemMap.get(item.getBarCode());
                    if (addedItem == null) {
                        Item tempItem = new Item(item);
                        addedItemMap.put(tempItem.getBarCode(), tempItem);
                    } else {
                        //添加了的直接修改量
                        addedItem.setAmount(addedItem.getAmount() + item.getAmount());
                    }
                }
        }

        //将map转为list
        for (String key : addedItemMap.keySet()) {
            payList.add(addedItemMap.get(key));
        }
    }

    /**
     * 读取json文件并转换为list集合
     *
     * @param filePath 文件路径
     * @param clazz    文件类对象
     * @param <T>      泛型参数
     * @return list集合
     */
    private <T> List<T> readData(String filePath, Class<T> clazz) throws Exception{
        FileChannel inChannel = null;
        String jsonStr = "";
        try {
            File file = new File(filePath);
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            inChannel = raf.getChannel();
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());

            //解码
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer charBuffer = decoder.decode(buffer);
            jsonStr = charBuffer.toString();
            raf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (inChannel != null) {
                    inChannel.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JSON.parseArray(jsonStr, clazz);
    }
}
