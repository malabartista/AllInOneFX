/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.dao;

import com.allinonefx.models.User;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
 
public class UserIbatisDAO {
 
    private SqlSessionFactory sqlSessionFactory = null;
 
    public UserIbatisDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }
 
    /**
     * Returns the list of all User instances from the database.
     * @return the list of all User instances from the database.
     */
    @SuppressWarnings("unchecked")
    public  List<User> selectAll(){
        List<User> list = null;
        SqlSession session = sqlSessionFactory.openSession();
 
        try {
            list = session.selectList("User.selectAll");
        } finally {
            session.close();
        }
        System.out.println("selectAll() --> "+list);
        return list;
 
    }
    /**
     * Select instance of User from the database.
     * @param user the instance to be persisted.
     */
   public User selectById(int id){
        User user = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            user = session.selectOne("User.selectById", id);
 
        } finally {
            session.close();
        }
        System.out.println("selectById("+id+") --> "+user);
        return user;
    } 
    /**
     * Insert an instance of User into the database.
     * @param user the instance to be persisted.
     */
   public int insert(User user){
       int id = -1;
        SqlSession session = sqlSessionFactory.openSession();
 
        try {
            id = session.insert("User.insert", user);
        } finally {
            session.commit();
            session.close();
        }
        System.out.println("insert("+user+") --> "+user.id.get());
        return id;
    }
    /**
   * Update an instance of User into the database.
   * @param user the instance to be persisted.
   */
    public void update(User user){
       int id = -1;
      SqlSession session = sqlSessionFactory.openSession();
 
      try {
          id = session.update("User.update", user);
 
      } finally {
          session.commit();
          session.close();
      }
      System.out.println("update("+user+") --> updated");
  }
 
    /**
     * Delete an instance of User from the database.
     * @param id value of the instance to be deleted.
     */
    public void delete(int id){
 
        SqlSession session = sqlSessionFactory.openSession();
 
        try {
            session.delete("User.delete", id);
        } finally {
            session.commit();
            session.close();
        }
        System.out.println("delete("+id+")");
 
    }
}