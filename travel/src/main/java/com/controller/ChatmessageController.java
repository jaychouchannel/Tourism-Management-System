package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.ChatmessageEntity;
import com.entity.view.ChatmessageView;

import com.service.ChatmessageService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 消息表
 * 后端接口
 * @author 
 * @email 
 * @date 2024-02-25 14:46:56
 */
@RestController
@RequestMapping("/chatmessage")
public class ChatmessageController {
    @Autowired
    private ChatmessageService chatmessageService;




    



    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ChatmessageEntity chatmessage,
		HttpServletRequest request){
        EntityWrapper<ChatmessageEntity> ew = new EntityWrapper<ChatmessageEntity>();

		PageUtils page = chatmessageService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, chatmessage), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ChatmessageEntity chatmessage, 
		HttpServletRequest request){
        EntityWrapper<ChatmessageEntity> ew = new EntityWrapper<ChatmessageEntity>();

		PageUtils page = chatmessageService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, chatmessage), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 消息列表
     */
    @RequestMapping("/mlist")
    public R mlist(@RequestParam Map<String, Object> params,ChatmessageEntity chatmessage, HttpServletRequest request){
        EntityWrapper<ChatmessageEntity> ew = new EntityWrapper<ChatmessageEntity>();
        ew.eq("uid", chatmessage.getUid()).eq("fid", chatmessage.getFid()).orNew().eq("fid", chatmessage.getUid()).eq("uid", chatmessage.getFid());
        PageUtils page = chatmessageService.queryPage(params, ew);
        chatmessageService.updateForSet("isread=1", new EntityWrapper<ChatmessageEntity>().eq("isread", 0).eq("fid", chatmessage.getUid()));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ChatmessageEntity chatmessage){
       	EntityWrapper<ChatmessageEntity> ew = new EntityWrapper<ChatmessageEntity>();
      	ew.allEq(MPUtil.allEQMapPre( chatmessage, "chatmessage")); 
        return R.ok().put("data", chatmessageService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ChatmessageEntity chatmessage){
        EntityWrapper< ChatmessageEntity> ew = new EntityWrapper< ChatmessageEntity>();
 		ew.allEq(MPUtil.allEQMapPre( chatmessage, "chatmessage")); 
		ChatmessageView chatmessageView =  chatmessageService.selectView(ew);
		return R.ok("查询消息表成功").put("data", chatmessageView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ChatmessageEntity chatmessage = chatmessageService.selectById(id);
        return R.ok().put("data", chatmessage);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ChatmessageEntity chatmessage = chatmessageService.selectById(id);
        return R.ok().put("data", chatmessage);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ChatmessageEntity chatmessage, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(chatmessage);
        chatmessageService.insert(chatmessage);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ChatmessageEntity chatmessage, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(chatmessage);
        chatmessageService.insert(chatmessage);
        return R.ok();
    }



     /**
     * 获取用户密保
     */
    @RequestMapping("/security")
    @IgnoreAuth
    public R security(@RequestParam String username){
        ChatmessageEntity chatmessage = chatmessageService.selectOne(new EntityWrapper<ChatmessageEntity>().eq("", username));
        return R.ok().put("data", chatmessage);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody ChatmessageEntity chatmessage, HttpServletRequest request){
        //ValidatorUtils.validateEntity(chatmessage);
        chatmessageService.updateById(chatmessage);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        chatmessageService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	
	/**
     * 前端智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,ChatmessageEntity chatmessage, HttpServletRequest request,String pre){
        EntityWrapper<ChatmessageEntity> ew = new EntityWrapper<ChatmessageEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = entry.getKey();
			if (pre.endsWith(".")) {
				newMap.put(pre + newKey, entry.getValue());
			} else if (StringUtils.isEmpty(pre)) {
				newMap.put(newKey, entry.getValue());
			} else {
				newMap.put(pre + "." + newKey, entry.getValue());
			}
		}
		params.put("sort", "clicktime");
        params.put("order", "desc");
		PageUtils page = chatmessageService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, chatmessage), params), params));
        return R.ok().put("data", page);
    }










}
