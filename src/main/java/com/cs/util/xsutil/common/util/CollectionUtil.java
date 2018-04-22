package com.cs.util.xsutil.common.util;


import java.util.*;
import java.util.function.Predicate;

/**
 * Created by xzh on 2016/7/26.
 */
public class CollectionUtil {
    public static boolean isBlank(Collection collection){
        boolean b = false;
        if(collection==null || collection.size()==0){
            b = true;
        }
        return b;
    }

    public static boolean isNotBlank(Collection collection){
        return !isBlank(collection);
    }

    public static Collection removeEmpty(Collection collection){
        collection.removeIf(new Predicate<String>() {
            @Override
            public boolean test(String item) {
                return StringUtil.isBlank(item);
            }
        });
        return collection;
    }

    public static List removeConstans(List obj){
        Set set = new HashSet<>();
        set.addAll(obj);
        obj.clear();
        obj.addAll(set);
        return obj;
    }
    public static  List newList(List list){
        if(list==null){
            return new ArrayList();
        }
        return list;
    }

    /**
     * 补全数组到多少个
     * @return
     */
    public static List addAllList(List list,int num){
         List tempO=new ArrayList();
         tempO.addAll(list);
            for (Object o : list) {
                if(tempO.size()<num){
                    tempO.add(o);
                }else{
                    return tempO;
                }
            }
            return addAllList(tempO,num);
        }
}
