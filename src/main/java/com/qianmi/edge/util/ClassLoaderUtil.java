package com.qianmi.edge.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.qianmi.edge.InterfaceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassLoaderUtil.java
 */

/**
 * <p>
 * 功能描述
 * </p>
 * @author Angus
 * @version 1.0
 * @since 1.0
 */
public final class ClassLoaderUtil {
    /** URLClassLoader的addURL方法 */
    private static Method addURL = initAddMethod();

    private static URLClassLoader system = (URLClassLoader) ClassLoader.getSystemClassLoader();

    private static Logger logger = LoggerFactory.getLogger(ClassLoaderUtil.class);


    static JSONSerializer serializer  = new JSONSerializer();
    /** 初始化方法 */
    private static final Method initAddMethod() {
        try {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            add.setAccessible(true);
            return add;
        } catch (Exception e) {
            logger.error("Get addURL method failed.", e);
        }
        return null;
    }

    /**
     * 循环遍历目录，找出所有的JAR包
     */
    private static final void loopFiles(File file, List<File> files) {
        if (file.isDirectory()) {
            File[] tmps = file.listFiles();
            for (File tmp : tmps) {
                loopFiles(tmp, files);
            }
        } else {
            if (file.getAbsolutePath().endsWith(".jar") || file.getAbsolutePath().endsWith(".zip")) {
                files.add(file);
            }
        }
    }

    /**
     * <pre>
     * 加载JAR文件
     * </pre>
     * 
     * @param file
     */
    public static final void loadJarFile(File file) {

        if (file == null) {
            return;
        }
        try {
            addURL.invoke(system, new Object[] { file.toURI().toURL() });
            logger.debug("成功加载{}包：", new Object[] { file.getAbsolutePath() });
        } catch (Exception e) {
            logger.error("{}包加载失败.", new Object[] { file.getAbsolutePath(), e });
        }
    }

    /**
     * <pre>
     * 从一个目录加载所有JAR文件
     * </pre>
     * 
     * @param path
     */
    public static final void loadJarPath(String path) {
        List<File> files = new ArrayList<File>();
        File lib = new File(path);
        loopFiles(lib, files);
        for (File file : files) {
            loadJarFile(file);
//            try {
//                loadClass(file.toURI().toURL().getPath());
//            }
//            catch (Exception ex){}
        }
        //loadClass( );
    }
//public static void loadClass(String path)
//{
//    try {
//
//        URL FileSysUrl = new URL(String.format("jar:file:%s!/",path));
//        JarURLConnection jarURLConnection = (JarURLConnection) FileSysUrl.openConnection();
//         if(null != jarURLConnection){
//                        JarFile jarFile = jarURLConnection.getJarFile();
//                        Enumeration<JarEntry> jarEntries = jarFile.entries();
//                        while (jarEntries.hasMoreElements()){
//                            JarEntry jarEntry = jarEntries.nextElement();
//                            String jarEntryName = jarEntry.getName();
//                            if(jarEntryName.endsWith(".class")){
//                                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
//                                //doAddClass(classSet, className);
//                                Object obj=null;
//                                try {
//
//                                    Class<?> clazz = Class.forName(className);
//                                    if (InterfaceLoader.isWrapClass(clazz) || clazz.isEnum() || clazz.isInterface()) {
//
//                                    } else if (clazz.isArray()) {
//                                        obj = new Object[0];
//                                    } else {
//                                        obj = clazz.newInstance();
//                                    }
//if (obj!=null){
//serializer.getMapping().
//}
//                            } catch (Exception e) {
//                                logger.warn("can not found bean {} in context", className);
//                            }
//
//                                System.out.println(className);
//                            }
//                        }
//                    }
//    } catch (IOException e) {
//        logger.error("get class set failure",e);
//    }
//}
}
