package com.offcn.project.service;

import com.offcn.project.enums.ProjectStatusEnume;
import com.offcn.project.pojo.*;
import com.offcn.project.vo.req.ProjectRedisStorageVo;

import java.util.List;

public interface ProjectCreateService {

    public String initCreateProject(Integer memberId);

    public void saveProjectInfo(ProjectStatusEnume auth, ProjectRedisStorageVo redisStorageVo);

    List<TReturn> getReturnList(Integer projectId);

    List<TProject> findAllProject();

    List<TProjectImages> getProjectImages(Integer id);

    TProject findById(Integer projectId);

    List<TTag> getAllTags();

    List<TType> getAllType();

    TReturn getReturnById(Integer returnId);
}
