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

import com.entity.DiscussremenjingdianEntity;
import com.entity.view.DiscussremenjingdianView;

import com.service.DiscussremenjingdianService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 热门景点评论表
 * 后端接口
 * @author 
 * @email 
 * @date 2024-02-25 14:46:57
 */
@RestController
@RequestMapping("/discussremenjingdian")
public class DiscussremenjingdianController {
    @Autowired
    private DiscussremenjingdianService discussremenjingdianService;




    



    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,DiscussremenjingdianEntity discussremenjingdian,
		HttpServletRequest request){
        EntityWrapper<DiscussremenjingdianEntity> ew = new EntityWrapper<DiscussremenjingdianEntity>();

		PageUtils page = discussremenjingdianService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discussremenjingdian), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,DiscussremenjingdianEntity discussremenjingdian, 
		HttpServletRequest request){
        EntityWrapper<DiscussremenjingdianEntity> ew = new EntityWrapper<DiscussremenjingdianEntity>();

		PageUtils page = discussremenjingdianService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discussremenjingdian), params), params));
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( DiscussremenjingdianEntity discussremenjingdian){
       	EntityWrapper<DiscussremenjingdianEntity> ew = new EntityWrapper<DiscussremenjingdianEntity>();
      	ew.allEq(MPUtil.allEQMapPre( discussremenjingdian, "discussremenjingdian")); 
        return R.ok().put("data", discussremenjingdianService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DiscussremenjingdianEntity discussremenjingdian){
        EntityWrapper< DiscussremenjingdianEntity> ew = new EntityWrapper< DiscussremenjingdianEntity>();
 		ew.allEq(MPUtil.allEQMapPre( discussremenjingdian, "discussremenjingdian")); 
		DiscussremenjingdianView discussremenjingdianView =  discussremenjingdianService.selectView(ew);
		return R.ok("查询热门景点评论表成功").put("data", discussremenjingdianView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DiscussremenjingdianEntity discussremenjingdian = discussremenjingdianService.selectById(id);
        return R.ok().put("data", discussremenjingdian);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DiscussremenjingdianEntity discussremenjingdian = discussremenjingdianService.selectById(id);
        return R.ok().put("data", discussremenjingdian);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DiscussremenjingdianEntity discussremenjingdian, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(discussremenjingdian);
        discussremenjingdianService.insert(discussremenjingdian);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DiscussremenjingdianEntity discussremenjingdian, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(discussremenjingdian);
        discussremenjingdianService.insert(discussremenjingdian);
        return R.ok();
    }



     /**
     * 获取用户密保
     */
    @RequestMapping("/security")
    @IgnoreAuth
    public R security(@RequestParam String username){
        DiscussremenjingdianEntity discussremenjingdian = discussremenjingdianService.selectOne(new EntityWrapper<DiscussremenjingdianEntity>().eq("", username));
        return R.ok().put("data", discussremenjingdian);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody DiscussremenjingdianEntity discussremenjingdian, HttpServletRequest request){
        //ValidatorUtils.validateEntity(discussremenjingdian);
        discussremenjingdianService.updateById(discussremenjingdian);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        discussremenjingdianService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	
	/**
     * 前端智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,DiscussremenjingdianEntity discussremenjingdian, HttpServletRequest request,String pre){
        EntityWrapper<DiscussremenjingdianEntity> ew = new EntityWrapper<DiscussremenjingdianEntity>();
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
		PageUtils page = discussremenjingdianService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discussremenjingdian), params), params));
        return R.ok().put("data", page);
    }










}
