package com.azer.disruptordemo.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 图片
 * @TableName picture
 */
@TableName(value ="picture")
@Data
public class Picture {
    /**
     * pictureId
     */
    @TableId(type = IdType.AUTO)
    private Long pictureId;
}
