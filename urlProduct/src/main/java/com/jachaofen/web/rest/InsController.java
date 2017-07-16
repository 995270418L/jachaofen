package com.jachaofen.web.rest;

import com.jachaofen.entities.basic.Item;
import com.jachaofen.entities.basic.SingleSource;
import com.jachaofen.service.GetUserImages;
import com.jachaofen.utils.UncheckExceptions;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by steve on 17-7-12.
 * ins 图片控制器
 */

@RestController
@RequestMapping("/api/v1/ins")
public class InsController {

    /**
     * 获取某一用户的首页配置
     * @param username 用户名
     * @return
     */
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public Map<String, Object> getPersonUrl(@PathVariable("username") String username){
        System.out.println("{username} method");
        Map<String, Object> map = new HashMap<>();
        List<Item> list = null;
        try{
            list = GetUserImages.getResources(username);
        }catch (UncheckExceptions e){
            List list2 = new ArrayList();
            list2.add(e.getStatusCode());
            list2.add(e.getError());
            map.put("error",list2);
            return map;
        }
        map.put("normal",list);
        return map;
    }

    /**
     * 获取下一页的json数据
     * @param username 用户名
     * @param max_id ?max_id=
     * @return
     */
    @RequestMapping(value = "/{username}/{max_id}", method = RequestMethod.GET)
    public Map<String, Object> getNextPage(@PathVariable("username") String username, @PathVariable("max_id") String max_id){
        System.out.println("/{username}/{max_id}");
        List<Item> list = GetUserImages.getResources(username, max_id);
        Map<String, Object> map = new HashMap<>();
        map.put("normal",list);
        return map;
    }

    /**
     * 获取指定post的数据
     * @param postId
     * @return
     */
    @RequestMapping(value = "p/{postId}", method = RequestMethod.GET)
    public Map<String, Object> getSingleSource(@PathVariable("postId") String postId){
        System.out.println("post data: " + postId);
        Map<String, Object> map = new HashMap<>();
        SingleSource singleSource = null;
        try {
            singleSource = GetUserImages.getSingleResource(postId);
            System.out.println(singleSource.toString());
        }catch (UncheckExceptions e){
            List list = new LinkedList();
            list.add(e.getStatusCode());
            list.add(e.getError());
            map.put("error",list);
            return map;
        }
        map.put("normal",singleSource);
        return map;
    }

}
