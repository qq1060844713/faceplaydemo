package com.dilusense.faceplaydemo.network.result;

/**
 * Created by Thinkpad on 2017/3/2.
 */
public class PayInfoResult {

    /**
     * id : 412727198512142612
     * id_type : 1
     * name : 张海林
     * gender : 男
     * birthday : 1985-12-14
     * ethnicity : 汉
     * image_url : /home/dilusense412727198512142612.jpg
     * similarity : 0.795266
     * tag : 一体化录入人员的同家族人员|一体化录入人员|不准出境人员|危安管控对象
     * leave_request : 1
     * data_source : 刑侦在逃
     * action : 发现即抓捕，同时通知原籍公安机关
     */

    private String id;
    private String name;
    private int similarity;
    private String id_card_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSimilarity() {
        return similarity;
    }

    public void setSimilarity(int similarity) {
        this.similarity = similarity;
    }

    public String getId_card_image() {
        return id_card_image;
    }

    public void setId_card_image(String id_card_image) {
        this.id_card_image = id_card_image;
    }
}
