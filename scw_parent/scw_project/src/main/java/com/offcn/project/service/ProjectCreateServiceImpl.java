package com.offcn.project.service;

import com.alibaba.fastjson.JSON;
import com.offcn.project.contants.ProjectConstant;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.enums.ProjectStatusEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService{

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TProjectMapper projectMapper;
    @Autowired
    private TProjectImagesMapper projectImagesMapper;
    @Autowired
    private TProjectTagMapper projectTagMapper;
    @Autowired
    private TProjectTypeMapper projectTypeMapper;
    @Autowired
    private TReturnMapper returnMapper;
    @Autowired
    private TTagMapper tagMapper;
    @Autowired
    private TTypeMapper typeMapper;

    public String initCreateProject(Integer memberId){
        ProjectRedisStorageVo vo = new ProjectRedisStorageVo();
        vo.setMemberid(memberId);
        String jsonStr = JSON.toJSONString(vo);
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+token,jsonStr);
        return token;
    }

    @Override
    public void saveProjectInfo(ProjectStatusEnume auth, ProjectRedisStorageVo redisStorageVo) {

        TProject project = new TProject();
        BeanUtils.copyProperties(redisStorageVo,project);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:SS");
        project.setCreatedate(sdf.format(new Date()));
        project.setStatus(auth.getCode()+"");
        projectMapper.insertSelective(project);
        Integer projectId = project.getId();
        String headerImage = redisStorageVo.getHeaderImage();
        TProjectImages projectImages = new TProjectImages(null,projectId,headerImage, ProjectImageTypeEnume.HEADER.getCode());
        projectImagesMapper.insertSelective(projectImages);
        List<String> imageList = redisStorageVo.getDetailsImage();
        if (!CollectionUtils.isEmpty(imageList)){
            for (String url : imageList) {
                TProjectImages detailImage = new TProjectImages(null,projectId,url,ProjectImageTypeEnume.DETAILS.getCode());
                projectImagesMapper.insertSelective(detailImage);
            }
        }
        List<Integer> tagIds = redisStorageVo.getTagids();
        for (Integer tagId : tagIds) {
            TProjectTag projectTag = new TProjectTag(null,projectId,tagId);
            projectTagMapper.insertSelective(projectTag);
        }
        List<Integer> typeIds = redisStorageVo.getTypeids();
        for (Integer typeId : typeIds) {
            TProjectType projectType = new TProjectType(null,projectId,typeId);
            projectTypeMapper.insertSelective(projectType);
        }
        List<TReturn> returnList = redisStorageVo.getProjectReturns();
        for (TReturn tReturn : returnList) {
            tReturn.setProjectid(projectId);
            returnMapper.insertSelective(tReturn);
        }
        redisTemplate.delete(ProjectConstant.TEMP_PROJECT_PREFIX+redisStorageVo.getProjectToken());
    }

    @Override
    public List<TReturn> getReturnList(Integer projectId) {

        TReturnExample example = new TReturnExample();
        TReturnExample.Criteria criteria = example.createCriteria();
        criteria.andProjectidEqualTo(projectId);

        return returnMapper.selectByExample(example);
    }

    @Override
    public List<TProject> findAllProject() {
        return projectMapper.selectByExample(null);
    }

    @Override
    public List<TProjectImages> getProjectImages(Integer id) {
        TProjectImagesExample example = new TProjectImagesExample();
        TProjectImagesExample.Criteria criteria = example.createCriteria();
        criteria.andProjectidEqualTo(id);
        return projectImagesMapper.selectByExample(example);
    }

    @Override
    public TProject findById(Integer projectId) {
        return projectMapper.selectByPrimaryKey(projectId);
    }

    @Override
    public List<TTag> getAllTags() {
        return tagMapper.selectByExample(null);
    }

    @Override
    public List<TType> getAllType() {
        return typeMapper.selectByExample(null);
    }

    @Override
    public TReturn getReturnById(Integer returnId) {

        return returnMapper.selectByPrimaryKey(returnId);
    }

}
