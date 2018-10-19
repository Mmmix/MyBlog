package com.wust.myblog.service;

import com.wust.myblog.modal.Result;
import com.wust.myblog.modal.Tag;

public interface ITagService {

    Result addTag(Tag tag);

    Result deleteTag(Tag tag);

    Result selectTagById(Integer id);

    Result selectAll();

    Result updateTag(Tag tag);


}
