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

import com.entity.FriendEntity;
import com.entity.view.FriendView;

import com.service.FriendService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 好友表
 * 后端接口
 * @author 
 * @email 
 * @date 2024-02-25 14:46:56
 */
@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;




    


     /**
     * 后端列表
     */
    @RequestMapping("/page2")
    @IgnoreAuth
    public R page2(@RequestParam Map<String, Object> params, HttpServletRequest request){
        PageUtils page = friendService.queryFriendPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,FriendEntity friend,
		HttpServletRequest request){
        EntityWrapper<FriendEntity> ew = new EntityWrapper<FriendEntity>();

		PageUtils page = friendService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, friend), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,FriendEntity friend, 
		HttpServletRequest request){
        EntityWrapper<FriendEntity> ew = new EntityWrapper<FriendEntity>();

		PageUtils page = friendService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, friend), params), params));
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( FriendEntity friend){
       	EntityWrapper<FriendEntity> ew = new EntityWrapper<FriendEntity>();
      	ew.allEq(MPUtil.allEQMapPre( friend, "friend")); 
        return R.ok().put("data", friendService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(FriendEntity friend){
        EntityWrapper< FriendEntity> ew = new EntityWrapper< FriendEntity>();
 		ew.allEq(MPUtil.allEQMapPre( friend, "friend")); 
		FriendView friendView =  friendService.selectView(ew);
		return R.ok("查询好友表成功").put("data", friendView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        FriendEntity friend = friendService.selectById(id);
        return R.ok().put("data", friend);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        FriendEntity friend = friendService.selectById(id);
        return R.ok().put("data", friend);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody FriendEntity friend, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(friend);
        friendService.insert(friend);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody FriendEntity friend, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(friend);
        friendService.insert(friend);
        return R.ok();
    }



     /**
     * 获取用户密保
     */
    @RequestMapping("/security")
    @IgnoreAuth
    public R security(@RequestParam String username){
        FriendEntity friend = friendService.selectOne(new EntityWrapper<FriendEntity>().eq("", username));
        return R.ok().put("data", friend);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody FriendEntity friend, HttpServletRequest request){
        //ValidatorUtils.validateEntity(friend);
        friendService.updateById(friend);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        friendService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	
	/**
     * 前端智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,FriendEntity friend, HttpServletRequest request,String pre){
        EntityWrapper<FriendEntity> ew = new EntityWrapper<FriendEntity>();
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
		PageUtils page = friendService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, friend), params), params));
        return R.ok().put("data", page);
    }










}
