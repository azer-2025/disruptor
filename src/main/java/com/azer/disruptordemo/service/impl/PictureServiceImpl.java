package com.azer.disruptordemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.azer.disruptordemo.domain.Picture;
import com.azer.disruptordemo.service.PictureService;
import com.azer.disruptordemo.mapper.PictureMapper;
import org.springframework.stereotype.Service;

/**
* @author a1798
* @description 针对表【picture(图片)】的数据库操作Service实现
* @createDate 2025-12-13 11:04:52
*/
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService{

}




