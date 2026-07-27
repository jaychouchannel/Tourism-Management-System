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

import com.entity.DaoyouEntity;
import com.entity.view.DaoyouView;

import com.service.DaoyouService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 导游
 * 后端接口
 * @author
 * @email
 * @date 2024-02-25 14:46:55
 */
@RestController
@RequestMapping("/daoyou")
public class DaoyouController {
    @Autowired
    private DaoyouService daoyouService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录
     */
    @IgnoreAuth
    @RequestMapping(value = "/login")
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        DaoyouEntity u = daoyouService.selectOne(new EntityWrapper<DaoyouEntity>().eq("daoyougonghao", username));
        if(u!=null && u.getStatus().intValue()==1) {
            return R.error("账号已锁定，请联系管理员。");
        }
        if(u==null || !u.getMima().equals(password)) {
            if(u!=null) {
                u.setPasswordwrongnum(u.getPasswordwrongnum()+1);
                if(u.getPasswordwrongnum()>=3) {
                    u.setStatus(1);
                }
                daoyouService.updateById(u);
            }
            return R.error("账号或密码不正确");
        }

        String token = tokenService.generateToken(u.getId(), username,"daoyou",  "导游" );
        return R.ok().put("token", token);
    }

    /**
     * 注册
     */
    @IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody DaoyouEntity daoyou){
        DaoyouEntity u = daoyouService.selectOne(new EntityWrapper<DaoyouEntity>().eq("daoyougonghao", daoyou.getDaoyougonghao()));
        if(u!=null) {
            return R.error("注册用户已存在");
        }
        Long uId = new Date().getTime();
        daoyou.setId(uId);
        daoyouService.insert(daoyou);
        return R.ok();
    }

    /**
     * 退出
     */
    @RequestMapping("/logout")
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.ok("退出成功");
    }

    /**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request){
        Long id = (Long)request.getSession().getAttribute("userId");
        DaoyouEntity u = daoyouService.selectById(id);
        return R.ok().put("data", u);
    }

    /**
     * 密码重置
     */
    @RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return R.error(401, "请先登录");
        }
        String currentUsername = (String) request.getSession().getAttribute("username");
        if (!username.equals(currentUsername)) {
            return R.error("只能重置本人密码");
        }
        DaoyouEntity u = daoyouService.selectOne(new EntityWrapper<DaoyouEntity>().eq("daoyougonghao", username));
        if(u==null) {
            return R.error("账号不存在");
        }
        String newPassword = java.util.UUID.randomUUID().toString().substring(0, 8);
        u.setMima(newPassword);
        daoyouService.updateById(u);
        return R.ok("密码已重置");
    }

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,DaoyouEntity daoyou,
        HttpServletRequest request){
        EntityWrapper<DaoyouEntity> ew = new EntityWrapper<DaoyouEntity>();

        PageUtils page = daoyouService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, daoyou), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,DaoyouEntity daoyou,
        HttpServletRequest request){
        EntityWrapper<DaoyouEntity> ew = new EntityWrapper<DaoyouEntity>();

        PageUtils page = daoyouService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, daoyou), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( DaoyouEntity daoyou){
        EntityWrapper<DaoyouEntity> ew = new EntityWrapper<DaoyouEntity>();
        ew.allEq(MPUtil.allEQMapPre( daoyou, "daoyou"));
        return R.ok().put("data", daoyouService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DaoyouEntity daoyou){
        EntityWrapper< DaoyouEntity> ew = new EntityWrapper< DaoyouEntity>();
        ew.allEq(MPUtil.allEQMapPre( daoyou, "daoyou"));
        DaoyouView daoyouView =  daoyouService.selectView(ew);
        return R.ok("查询导游成功").put("data", daoyouView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DaoyouEntity daoyou = daoyouService.selectById(id);
        return R.ok().put("data", daoyou);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DaoyouEntity daoyou = daoyouService.selectById(id);
        return R.ok().put("data", daoyou);
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DaoyouEntity daoyou, HttpServletRequest request){
        if(daoyouService.selectCount(new EntityWrapper<DaoyouEntity>().eq("daoyougonghao", daoyou.getDaoyougonghao()))>0) {
            return R.error("导游工号已存在");
        }
        daoyou.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
        DaoyouEntity u = daoyouService.selectOne(new EntityWrapper<DaoyouEntity>().eq("daoyougonghao", daoyou.getDaoyougonghao()));
        if(u!=null) {
            return R.error("用户已存在");
        }
        daoyou.setId(new Date().getTime());
        daoyouService.insert(daoyou);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DaoyouEntity daoyou, HttpServletRequest request){
        if(daoyouService.selectCount(new EntityWrapper<DaoyouEntity>().eq("daoyougonghao", daoyou.getDaoyougonghao()))>0) {
            return R.error("导游工号已存在");
        }
        daoyou.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
        DaoyouEntity u = daoyouService.selectOne(new EntityWrapper<DaoyouEntity>().eq("daoyougonghao", daoyou.getDaoyougonghao()));
        if(u!=null) {
            return R.error("用户已存在");
        }
        daoyou.setId(new Date().getTime());
        daoyouService.insert(daoyou);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody DaoyouEntity daoyou, HttpServletRequest request){
        if(daoyouService.selectCount(new EntityWrapper<DaoyouEntity>().ne("id", daoyou.getId()).eq("daoyougonghao", daoyou.getDaoyougonghao()))>0) {
            return R.error("导游工号已存在");
        }
        daoyouService.updateById(daoyou);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        daoyouService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}
