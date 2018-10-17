package com.wust.myblog.util;

import com.wust.myblog.common.ResultEnum;
import com.wust.myblog.modal.Result;

public class ResultUtil {

    /**成功且带数据**/
    public static Result success(Object object){
        Result result = new Result();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        result.setData(object);
        return result;
    }
    /**成功但不带数据**/
    public static Result success(){

        return success(null);
    }
    public static Result error(String msg){
        Result result = new Result();
        result.setCode(ResultEnum.UNKNOWN_ERROR.getCode());
        result.setMsg(msg);
        return result;
    }
    /**失败**/
    public static Result error(Integer code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}

