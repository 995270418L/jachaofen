package com.jachaofen.service;

import com.alibaba.fastjson.JSON;
import com.jachaofen.entities.basic.ErrorExtend;
import com.jachaofen.entities.basic.Item;
import com.jachaofen.entities.basic.SingleSource;
import com.jachaofen.entities.basic.User;
import com.jachaofen.entities.extend.*;
import com.jachaofen.utils.PersonUtils;
import com.jachaofen.utils.UncheckExceptions;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 17-7-11.
 * 根据前端的调用，一页一页的获取用户的所有url(传进来的必须是用户名)
 */
public class GetUserImages {

    private static String MEDIAURL = "https://www.instagram.com/username/media";
    private static String POSTBASEURL = "https://www.instagram.com/p/";
    private static String responseText = "";
    private static String max_id = "";

    public static User getUserInfo(String username){
        System.out.println("get User Info according username");
        if(StringUtils.isNotBlank(username)){
            String mediaUrl = MEDIAURL.replace("username", username);
            String text = Requests.get(mediaUrl).send().readToText();
            Page page = JSON.parseObject(text, Page.class);
            User user = page.getItems().get(0).getUser();
            return user;
        }
        return null;
    }

    /**
     * 根据用户名获取响应的responseText
     * @param username
     * @return
     */
    public static String getResponseText(String username){
         return getResponseText(username,null);
    }

    /**
     * 根据用户名和max_id获取响应的responseText
     * @param username
     * @param max_id page.getItems().get(-1).getId() 获取max_id
     * @return
     */
    public static String getResponseText(String username, String max_id) throws UncheckExceptions{
        RawResponse resp = null;
        if(StringUtils.isNotBlank(max_id)){
            resp =  Requests.get(MEDIAURL.replace("username", username) + "?&max_id=" + max_id).send();
        }
        resp =  Requests.get(MEDIAURL.replace("username", username)).send();
        if(resp.getStatusCode() == 404){
            throw new UncheckExceptions(404, new ErrorExtend("用户不存在","user not exists"));
        }
        return resp.readToText();
    }

    /**
     * 根据用户名获取resource
     * @param username
     * @return
     */
    public static List<Item> getResources(String username) throws UncheckExceptions{
        return getResources(username,null);
    }

    /**
     * 根据用户名和max_id获取resource
     * @param username
     * @param max_id
     * @return
     */
    public static List<Item> getResources(String username, String max_id) throws UncheckExceptions{

        responseText = getResponseText(username, max_id);
        Page page = JSON.parseObject(responseText, Page.class);
        if(page.getItems().size() < 10){
            throw new UncheckExceptions(400, new ErrorExtend("该用户为私密用户","This Account is Private"));
        }
        return getResourceList(page);
    }

    /**
     * 专门解析page实体的，
     * @param page 传入page实体类
     * @return 返回一个ArrayLists
     */
    public static List<Item> getResourceList(Page page){
        List<Item> lists = new ArrayList<>();
        String url = "", content = "", time = "",video_url = "";
        Item resource = null;
        if(page.isMore_available()){
            max_id = page.getItems().get(19).getId();
        }
        for (Items_single item : page.getItems()) {
            if (item.getCarousel_media() != null) {
                for(Carousel_media_single images : item.getCarousel_media()){
                    url = PersonUtils.accessFilter(images.getImages().getStandard_resolution().getUrl());
                    resource = new Item(url);
                    lists.add(resource);
                }
            } else if(item.getType().equals("image")){

                url = PersonUtils.accessFilter(item.getImages().getStandard_resolution().getUrl());
                content = PersonUtils.accessNewLine(item.getCaption().getText());
                time = PersonUtils.accessTime(item.getCreated_time());
                resource = new Item(time,content,url, max_id);
            }else if(item.getType().equals("video")){

                url = PersonUtils.accessFilter(item.getVideos().getStandard_resolution().getUrl());
                video_url = PersonUtils.accessFilter(item.getVideos().getStandard_resolution().getUrl());
                content = PersonUtils.accessNewLine(item.getCaption().getText());
                time = PersonUtils.accessTime(item.getCreated_time());
                resource = new Item(time, content, url, max_id);
                resource.setVideo_url(video_url);
            }else{

            }
            lists.add(resource);
        }
        return lists;
    }


    /**
     * 获取复制的帖子地址
     * @param postId eg: 只需一个postid, 其他的数据会自动填充进来
     * @return
     */
    public static SingleSource getSingleResource(String postId)throws UncheckExceptions{
        String url = POSTBASEURL + postId + "/";
        responseText = Requests.get(url).send().readToText();
        SingleSource ss = null;
        try{
            String share_data = responseText.split("window._sharedData = ")[1].split(";</script>")[0];
            String display_url = share_data.split("\"display_url\": \"")[1].split("\",")[0];
            String caption = null;
            try {
                String hasCaption = share_data.split("\"node\": \\{\"text\": \"")[1];
                caption = PersonUtils.accessContent(share_data.split("\"text\": \"")[1].split("\"}}]},")[0]);
            }catch (ArrayIndexOutOfBoundsException e){
                caption = null;
            }
            String is_video = share_data.split("\"is_video\": ")[1].split(",")[0];
            ss = new SingleSource(display_url, caption, is_video);
            if(is_video.equals("true")){
                String video_url = share_data.split("\"video_url\": \"")[1].split("\",")[0];
                ss.setVideo_url(video_url);
            }
        }catch(ArrayIndexOutOfBoundsException e){
            throw new UncheckExceptions(500, new ErrorExtend("对不起，服务错误，请稍候重试","sorry, service not response, please try again."));
        }
        return ss;
    }


   public static void main(String... args){
       long startTime = System.currentTimeMillis();
       /* List<Item> list1 = getResources("xxxxxxxxxx");
        //List<Item> list2 = getResources("gem0816", max_id);
        // 下一页
        System.out.println("use time in get Pics : " + (System.currentTimeMillis() - startTime));
        System.out.println(max_id);*/
        SingleSource ss  = getSingleResource("BWj_c9Blfsm");
        System.out.println(ss.toString());
    }

}
