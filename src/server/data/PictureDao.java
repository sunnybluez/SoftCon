package server.data;

import server.entity.Picture;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PictureDao {

    private final static String dataPath= "images";

    static {
        File file = new File(dataPath);
        if(!file.exists()){
            file.mkdir();
        }
    }


    public static Picture findPicture(String picId){
        Picture picRead = null;
        try {
            FileInputStream picIn = new FileInputStream(convertToPath(picId));
            ObjectInputStream picInSt = new ObjectInputStream(picIn);

            picRead = (Picture) picInSt.readObject();
            picInSt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return picRead;
    }

    public static List<String> findAllPicId(){
        List<String> picIdList = new ArrayList<>();
        File dirFile = new File(dataPath);
        if (dirFile.exists()) {
            File[] files = dirFile.listFiles();
            if (files != null) {
                for (File fileChildDir : files) {
//                    System.out.println(fileChildDir.getName());
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

    public static void updatePicture(Picture picture){

        try {
            FileOutputStream picOut = new FileOutputStream(convertToPath(picture.getId()));
            ObjectOutputStream picOutSt = new ObjectOutputStream(picOut);
            picOutSt.writeObject(picture);
            picOutSt.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //将pictureid转换为储存路径
    private static String convertToPath(String pictureId){
        return dataPath + File.separator + pictureId + ".txt";
    }


}
