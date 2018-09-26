package server.service;

import client.presentation.quantity.Contants;
import client.presentation.quantity.GraphType;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import server.data.PictureDao;
import server.entity.Picture;

import java.awt.image.BufferedImage;
import java.util.List;

public class PictureService {


    public static String recognize(Image image){
        BufferedImage bi = (BufferedImage) SwingFXUtils.fromFXImage(image, null);

        //获取图像的宽度和高度
        int width = bi.getWidth();
        int height = bi.getHeight();



        int count = 0;
        int top = 0;
        int buttom = 0;
        int left = 0;
        int right = 0;

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){//行扫描
                int dip = bi.getRGB(j, i);
                if(dip!= -1){
                    if(count==0){
                        top = i;
                        buttom = i;
                        left = j;
                        right = j;
                        count++;
                    }else {
                        if(buttom<i) buttom = i;
                        if(left>j) left = j;
                        if(right<j) right = j;
                    }
                }
            }

        }
        double area_rect = (buttom-top + 1) * (right - left +1);

        double area = 0;
        for(int i=0;i<height;i++) {
            int count_temp = 0;
            int left_j = -1;
            int right_j = -1;
            int count_line = 0;
            for (int j = 0; j < width; j++) {//行扫描
                int dip = bi.getRGB(j, i);

                if(dip != -1){

                    if(count_temp == 0){

                        left_j = j;
                        right_j = j;
                        count_temp++;
                    }

                    if(right_j < j){

                        right_j = j;
                    }
                    count_line++;
                }

            }
            

            if(left_j !=right_j){
                int row_all =  width - count_line - left_j - (width - right_j - 1);
                area += row_all;

            }

        }

        double area_cover_ratio = area/area_rect;
        GraphType type = null;

        if (area_cover_ratio >= Contants.RECT_LOW_RATIO) type = GraphType.Rect;
        if (area_cover_ratio< Contants.RECT_LOW_RATIO && area_cover_ratio > Contants.CIRCLE_LOW_RATIO ) type = GraphType.Circle;
        if (area_cover_ratio <Contants.CIRCLE_LOW_RATIO && area_cover_ratio >0)   type = GraphType.Triangle;
//        if (area_cover_ratio >= 0 && area_cover_ratio <= 0.3 )  type = "线";

        double width_rect = right - left  + 1;
        double height_rect = buttom - top + 1;
        double ratio_wh = width_rect/height_rect;
        if((type.equals(GraphType.Rect))&&Contants.WIDTH_HEIGHT_LOW_RATIO<=ratio_wh&&ratio_wh<=Contants.WIDTH_HEIGHT_HIGH_RATIO) type = GraphType.Square;

        if(type==null){
            return "";
        }else {
            return type.getType();
        }

    }



    public static void savePicture(Picture picture){
        PictureDao.updatePicture(picture);
    }

    public static List<String> findAllPicId(){
        List picList = PictureDao.findAllPicId();
        return  picList;
    }

    public static Picture findPicture(String picId){
        Picture picSelected = PictureDao.findPicture(picId);
        return picSelected;

    }




}
