package com.cs.util.xsutil.common.base;

import com.cs.util.xsutil.common.constans.Constans;
import com.cs.util.xsutil.common.base.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

public abstract class BaseService<E extends BaseEntity,A extends BaseDao<E>,D>{

    //基础dao
    @Autowired
    private A baseDao;

    public A getDao(){
        return baseDao;
    }

    /**
     * 保存
     * @param entity 要保存的对象
     * @return
     */
    public E save(E entity){
        return baseDao.save(entity);
    }

    /**
     * 查询所有这里过滤
     * @return
     */
    public List<E> findAll(){
        return baseDao.findAll();
    }

    /**
     * 查询所有的可以自己定义查询条件
     * @param specification
     * @return
     */
    public List<E> findAll(Specification<E> specification){
       return baseDao.findAll(specification);
    }

    /**
     * 查询所有的可以自己定义查询条件
     * @param specification
     * @return
     */
    public Page<E> findAll(Specification<E> specification, Pageable pageable){
        pageable = addSortToPageable(pageable);
        return baseDao.findAll(specification,pageable);
    }

    /**
     *
     * 分页不带条件
     * @return
     */
    public Page<E> findAll(Pageable pageable){
        pageable = addSortToPageable(pageable);
        return baseDao.findAll(pageable);
    }

    /**
     * 根据id查询所有
     * @param ids
     * @return
     */
    public List<E> findAll(Iterable<String> ids){
       return baseDao.findAllById(ids);
    }

    /**
     * 保存所有
     * @return
     */
    public List<E> save(Iterable<E> entities){
        return baseDao.saveAll(entities);
    }

    /**
     * 刷新
     */
    public void flush(){
        baseDao.flush();
    }

    /**
     * 保存并刷新
     */
    public E saveAndFlush(E entity){
        return baseDao.saveAndFlush(entity);
    }

    /**
     * 批量删除
     * @param entities
     */
    public void deleteInBatch(Iterable<E> entities){
        baseDao.deleteInBatch(entities);
    }

    /**
     * 删除所有
     */
    public void deleteAllInBatch(){
        baseDao.deleteAllInBatch();
    }

    /**
     * 获取一条
     * @param id
     * @return
     */
    public E getOne(String id){
        return baseDao.getOne(id);
    }

    public E getById(String id){
        return baseDao.getByObjId(id);
    }

    /**
     * 默认加上事件排序，避免导致代码混乱，这里这个方法不能公有。
     * @param pageable
     * @return
     */
    private Pageable addSortToPageable(Pageable pageable){
        Sort pageSort = pageable.getSort();
        if(pageSort == null){
            Sort.Order order = new Sort.Order(Sort.Direction.ASC, Constans.def_order_filed);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(order));
        }
        return pageable;
    }

    /**
     * 删除
     */
    public void delete(String id){
        baseDao.deleteById(id);
    }

    /**
     * 删除
     */
    public void delete(E entity){
        baseDao.delete(entity);
    }

    /**
     * 删除全部
     */
    public void deleteAll(){
        baseDao.deleteAll();
    }
}
