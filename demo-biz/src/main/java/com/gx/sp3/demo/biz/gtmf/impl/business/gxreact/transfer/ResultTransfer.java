package com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.transfer;

import com.gx.sp3.demo.api.model.BlogBlockDTO;
import com.gx.sp3.demo.api.model.BlogPageDTO;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.BlogBlockDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.BlogPageDO;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResultTransfer {
    public List<BlogBlockDTO> transferToBlogBlockDTO(List<BlogBlockDO> list) {
        if (list == null) {
            return null;
        }

        List<BlogBlockDTO> result = Lists.newArrayList();
        for (BlogBlockDO blogBlockDO : list) {
            BlogBlockDTO blogBlockDTO = new BlogBlockDTO();
            blogBlockDTO.setAuthor(blogBlockDO.getAuthor());
            blogBlockDTO.setId(blogBlockDO.getId());
            blogBlockDTO.setCommentCnt(blogBlockDO.getCommentCnt());
            blogBlockDTO.setImageUrl(blogBlockDO.getImageUrl());
            blogBlockDTO.setTitle(blogBlockDO.getTitle());
            blogBlockDTO.setPublishDate(blogBlockDO.getPublishDate());
            result.add(blogBlockDTO);
        }
        return result;
    }

    public BlogPageDTO transferToBlogPageDTO(BlogPageDO blogPageDO) {
        if (blogPageDO == null) {
            return null;
        }

        BlogPageDTO blogBlockDTO = new BlogPageDTO();
        blogBlockDTO.setBlogBlockDTOList(transferToBlogBlockDTO(blogPageDO.getBlogBlockDOList()));
        blogBlockDTO.setTitle(blogPageDO.getTitle());
        blogBlockDTO.setDesc(blogPageDO.getDesc());
        return blogBlockDTO;
    }
}
