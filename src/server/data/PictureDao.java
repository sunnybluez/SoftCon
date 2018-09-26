package server.data;

import server.entity.Picture;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 周正伟
 * @Description 数据类 处理IO操作
 */
public class PictureDao {

    private final static String picPath = "images";

    //初始化静态变量 picPath 存储序列化路径
    static {
        File file = new File(picPath);
        if(!file.exists()){
            file.mkdir();
        }
    }


    /**
     * @param picId 图片的ID
     * @return Picture数据类
     */
    public static Picture findPicture(String picId){
        Picture picRead = null;
        try {
            FileInputStream picIn = new FileInputStream(getUrl(picId));
            ObjectInputStream picInSt = new ObjectInputStream(picIn);

            picRead = (Picture) picInSt.readObject();
            picInSt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return picRead;
    }

    /**
     * @return  所有的图片ID列表
     */
    public static List<String> findAllPicId(){
        List<String> picIdList = new ArrayList<>();
        File dirFile = new File(picPath);
        if (dirFile.exists()) {
            File[] files = dirFile.listFiles();
            if (files != null) {
                for (File fileChildDir : files) {
                 if (fileChildDir.isFile()) {
                        picIdList.add(fileChildDir.getName().split("\\.")[0]);
                    }
                }
            }
        }else{
            System.out.println("你想查找的文件不存在");
        }

      return picIdList;
    }

    /**
     * @param picture 图片
     */
    public static void updatePicture(Picture picture){

        try {
            FileOutputStream picOut = new FileOutputStream(getUrl(picture.getId()));
            ObjectOutputStream picOutSt = new ObjectOutputStream(picOut);
            picOutSt.writeObject(picture);
            picOutSt.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * @param pictureId 图片ID
     * @return 图片路径url
     */
    private static String getUrl(String pictureId){
        return picPath + File.separator + pictureId + ".txt";
    }


}
